package userspacepacketdriver;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UserSpacePacketDriverLLD {

    // manages packet data with metadata
    @Data
    public class PacketBuffer {
        private ByteBuffer buffer;
        private int dataOffset;
        private int dataLength;
        private int packetLength;
        private long timestamp;
        private int refCount;
        private PacketBuffer next;
        private long flags;

        // Offload flags
        public static final long PKT_RX_VLAN = 1L << 0;
        public static final long PKT_RX_RSS_HASH = 1L << 1;
        public static final long PKT_RX_FDIR = 1L << 2;
        public static final long PKT_TX_OFFLOAD = 1L << 3;

        public PacketBuffer(int size) {
            this.buffer = ByteBuffer.allocateDirect(size);
            this.refCount = 1;
            reset();
        }

        public void reset() {
            buffer.clear();
            dataOffset = 0;
            dataLength = 0;
            packetLength = 0;
            refCount = 1;
            flags = 0;
            next = null;
            timestamp = System.nanoTime();
        }

        public ByteBuffer getData() {
            buffer.position(dataOffset);
            buffer.limit(dataOffset + dataLength);
            return buffer.slice();
        }

        public void setData(byte[] data, int offset, int length) {
            buffer.clear();
            buffer.put(data, offset, length);
            this.dataOffset = 0;
            this.dataLength = length;
            this.packetLength = length;
        }
    }

    // Pre-allocated buffer pool for zero-copy operations
    public class PacketMemoryPool {
        private final Queue<PacketBuffer> freeBuffers;
        private final int bufferSize;
        private final int poolSize;
        private final AtomicInteger allocatedCount;
        private final ReentrantLock lock;

        public PacketMemoryPool(int poolSize, int bufferSize) {
            this.poolSize = poolSize;
            this.bufferSize = bufferSize;
            this.freeBuffers = new ConcurrentLinkedQueue<>();
            this.allocatedCount = new AtomicInteger(0);
            this.lock = new ReentrantLock();

            // Pre-allocate buffers
            for (int i = 0; i < poolSize; i++) {
                freeBuffers.offer(new PacketBuffer(bufferSize));
            }
        }

        public PacketBuffer allocate() {
            PacketBuffer buffer = freeBuffers.poll();
            if (buffer != null) {
                buffer.reset();
                allocatedCount.incrementAndGet();
            }
            return buffer;
        }

        public void deallocate(PacketBuffer buffer) {
            if (buffer != null && buffer.getRefCount() <= 1) {
                buffer.reset();
                freeBuffers.offer(buffer);
                allocatedCount.decrementAndGet();
            }
        }

        public int getAvailableCount() {
            return freeBuffers.size();
        }

        public int getAllocatedCount() {
            return allocatedCount.get();
        }
    }

    // Lock-free ring buffer for high-performance packet queues
    public class PacketRing {
        private final PacketBuffer[] ring;
        private final int size;
        private final int mask;
        private volatile int head;
        private volatile int tail;
        private final AtomicInteger count;

        public PacketRing(int size) {
            // Ensure size is power of 2
            this.size = Integer.highestOneBit(size);
            this.mask = this.size - 1;
            this.ring = new PacketBuffer[this.size];
            this.head = 0;
            this.tail = 0;
            this.count = new AtomicInteger(0);
        }

        public boolean enqueue(PacketBuffer buffer) {
            if (count.get() >= size) {
                return false;
            }

            ring[head & mask] = buffer;
            head++;
            count.incrementAndGet();
            return true;
        }

        public PacketBuffer dequeue() {
            if (count.get() == 0) {
                return null;
            }

            PacketBuffer buffer = ring[tail & mask];
            ring[tail & mask] = null;
            tail++;
            count.decrementAndGet();
            return buffer;
        }

        public int enqueueBurst(PacketBuffer[] buffers, int count) {
            int enqueued = 0;
            for (int i = 0; i < count; i++) {
                if (!enqueue(buffers[i])) {
                    break;
                }
                enqueued++;
            }
            return enqueued;
        }

        public int dequeueBurst(PacketBuffer[] buffers, int maxCount) {
            int dequeued = 0;
            for (int i = 0; i < maxCount; i++) {
                PacketBuffer buffer = dequeue();
                if (buffer == null) {
                    break;
                }
                buffers[i] = buffer;
                dequeued++;
            }
            return dequeued;
        }

        public int getCount() {
            return count.get();
        }

        public int getFreeSpace() {
            return size - count.get();
        }
    }

    // Queue configuration
    @Data
    public class QueueConfig {
        private int queueId;
        private int ringSize;
        private PacketMemoryPool memoryPool;
        private boolean checksumOffload;
        private boolean rssEnabled;

        public QueueConfig(int queueId, int ringSize, PacketMemoryPool memoryPool) {
            this.queueId = queueId;
            this.ringSize = ringSize;
            this.memoryPool = memoryPool;
            this.checksumOffload = false;
            this.rssEnabled = false;
        }
    }

    @Getter
    public class PacketQueue {
        private final int queueId;
        private final PacketRing ring;
        private final PacketMemoryPool mempool;
        private final AtomicLong packetsProcessed;
        private final AtomicLong bytesProcessed;
        private final AtomicLong packetsDropped;
        private volatile boolean started;

        public PacketQueue(QueueConfig config) {
            this.queueId = config.getQueueId();
            this.ring = new PacketRing(config.getRingSize());
            this.mempool = config.getMemoryPool();
            this.packetsProcessed = new AtomicLong(0);
            this.bytesProcessed = new AtomicLong(0);
            this.packetsDropped = new AtomicLong(0);
            this.started = false;
        }

        public void start() {
            started = true;
        }

        public void stop() {
            started = false;
        }

        public int receivePackets(PacketBuffer[] packets, int maxPackets) {
            if (!started) return 0;

            int received = ring.dequeueBurst(packets, maxPackets);
            if (received > 0) {
                packetsProcessed.addAndGet(received);
                for (int i = 0; i < received; i++) {
                    bytesProcessed.addAndGet(packets[i].getPacketLength());
                }
            }
            return received;
        }

        public int transmitPackets(PacketBuffer[] packets, int count) {
            if (!started) return 0;

            int transmitted = ring.enqueueBurst(packets, count);
            if (transmitted < count) {
                packetsDropped.addAndGet(count - transmitted);
            }

            packetsProcessed.addAndGet(transmitted);
            for (int i = 0; i < transmitted; i++) {
                bytesProcessed.addAndGet(packets[i].getPacketLength());
            }

            return transmitted;
        }

        public boolean enqueuePacket(PacketBuffer packet) {
            if (!started) return false;
            return ring.enqueue(packet);
        }
    }

    @Getter
    @ToString
    class DeviceStats {
        private final AtomicLong rxPackets = new AtomicLong(0);
        private final AtomicLong txPackets = new AtomicLong(0);
        private final AtomicLong rxBytes = new AtomicLong(0);
        private final AtomicLong txBytes = new AtomicLong(0);
        private final AtomicLong rxErrors = new AtomicLong(0);
        private final AtomicLong txErrors = new AtomicLong(0);
        private final AtomicLong rxDropped = new AtomicLong(0);
        private final AtomicLong txDropped = new AtomicLong(0);

        public void incrementRxPackets(long count) { rxPackets.addAndGet(count); }
        public void incrementTxPackets(long count) { txPackets.addAndGet(count); }
        public void incrementRxBytes(long bytes) { rxBytes.addAndGet(bytes); }
        public void incrementTxBytes(long bytes) { txBytes.addAndGet(bytes); }
        public void incrementRxErrors() { rxErrors.incrementAndGet(); }
        public void incrementTxErrors() { txErrors.incrementAndGet(); }
        public void incrementRxDropped() { rxDropped.incrementAndGet(); }
        public void incrementTxDropped() { txDropped.incrementAndGet(); }

        public void reset() {
            rxPackets.set(0);
            txPackets.set(0);
            rxBytes.set(0);
            txBytes.set(0);
            rxErrors.set(0);
            txErrors.set(0);
            rxDropped.set(0);
            txDropped.set(0);
        }
    }

    @Getter
    @Setter
    class DeviceConfig {
        private int maxReceiveQueues;
        private int maxTransmitQueues;
        private boolean promiscuousMode;
        private boolean checksumOffload;
        private boolean rssEnabled;
        private int mtu;
        private byte[] macAddress;

        public DeviceConfig() {
            this.maxReceiveQueues = 1;
            this.maxTransmitQueues = 1;
            this.promiscuousMode = false;
            this.checksumOffload = false;
            this.rssEnabled = false;
            this.mtu = 1500;
            this.macAddress = new byte[6];
        }
    }

    public class UserspacePacketDriver {
        public enum DeviceState {
            DETACHED, ATTACHED, CONFIGURED, STARTED, STOPPED
        }

        private final int deviceId;
        @Getter
        private final String deviceName;
        @Getter
        private DeviceState state;
        private DeviceConfig config;
        // Statistics and monitoring
        @Getter
        private DeviceStats stats;

        private PacketQueue[] receiveQueues;
        private PacketQueue[] transmitQueues;
        @Getter
        private PacketMemoryPool memoryPool;

        // Network channel for actual packet I/O
        private DatagramChannel channel;
        private SocketAddress localAddress;
        private volatile boolean pollingEnabled;

        // Polling thread
        private ExecutorService pollingExecutor;
        private Future<?> pollingTask;

        public UserspacePacketDriver(int deviceId, String deviceName) {
            this.deviceId = deviceId;
            this.deviceName = deviceName;
            this.state = DeviceState.DETACHED;
            this.config = new DeviceConfig();
            this.stats = new DeviceStats();
            this.pollingEnabled = false;
        }

        // Device lifecycle management
        public boolean attach() {
            if (state != DeviceState.DETACHED) {
                return false;
            }

            try {
                // Create memory pool
                memoryPool = new PacketMemoryPool(8192, 2048);

                // Create UDP channel for packet I/O simulation
                channel = DatagramChannel.open();
                channel.configureBlocking(false);

                state = DeviceState.ATTACHED;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean configure(DeviceConfig deviceConfig) {
            if (state != DeviceState.ATTACHED) {
                return false;
            }

            this.config = deviceConfig;

            // Initialize queues
            receiveQueues = new PacketQueue[config.getMaxReceiveQueues()];
            transmitQueues = new PacketQueue[config.getMaxTransmitQueues()];

            for (int i = 0; i < config.getMaxReceiveQueues(); i++) {
                QueueConfig queueConfig = new QueueConfig(i, 1024, memoryPool);
                receiveQueues[i] = new PacketQueue(queueConfig);
            }

            for (int i = 0; i < config.getMaxTransmitQueues(); i++) {
                QueueConfig queueConfig = new QueueConfig(i, 1024, memoryPool);
                transmitQueues[i] = new PacketQueue(queueConfig);
            }

            state = DeviceState.CONFIGURED;
            return true;
        }

        public boolean start() {
            if (state != DeviceState.CONFIGURED) {
                return false;
            }

            try {
                // Bind to a local address
                localAddress = new InetSocketAddress("0.0.0.0", 0);
                channel.bind(localAddress);

                // Start queues
                for (PacketQueue queue : receiveQueues)
                    if (queue != null)
                        queue.start();

                for (PacketQueue queue : transmitQueues)
                    if (queue != null)
                        queue.start();

                // Start polling thread
                pollingEnabled = true;
                pollingExecutor = Executors.newSingleThreadExecutor();
                pollingTask = pollingExecutor.submit(this::pollingLoop);

                state = DeviceState.STARTED;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean stop() {
            if (state != DeviceState.STARTED) {
                return false;
            }

            // Stop polling
            pollingEnabled = false;
            if (pollingTask != null) {
                pollingTask.cancel(true);
            }
            if (pollingExecutor != null) {
                pollingExecutor.shutdown();
            }

            // Stop queues
            for (PacketQueue queue : receiveQueues) {
                if (queue != null) queue.stop();
            }
            for (PacketQueue queue : transmitQueues) {
                if (queue != null) queue.stop();
            }

            state = DeviceState.STOPPED;
            return true;
        }

        public void detach() {
            if (state == DeviceState.STARTED) {
                stop();
            }

            try {
                if (channel != null) {
                    channel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            state = DeviceState.DETACHED;
        }

        // Packet I/O operations
        public int receivePackets(int queueId, PacketBuffer[] packets, int maxPackets) {
            if (state != DeviceState.STARTED || queueId >= receiveQueues.length) {
                return 0;
            }

            return receiveQueues[queueId].receivePackets(packets, maxPackets);
        }

        public int transmitPackets(int queueId, PacketBuffer[] packets, int count) {
            if (state != DeviceState.STARTED || queueId >= transmitQueues.length) {
                return 0;
            }

            int transmitted = transmitQueues[queueId].transmitPackets(packets, count);

            // Actually transmit packets through the channel
            for (int i = 0; i < transmitted; i++) {
                transmitPacket(packets[i]);
            }

            return transmitted;
        }

        private void transmitPacket(PacketBuffer packet) {
            try {
                ByteBuffer data = packet.getData();
                // In a real implementation, you would extract destination address from packet
                // For demo, we'll use a default destination
                InetSocketAddress destination = new InetSocketAddress("127.0.0.1", 12345);
                channel.send(data, destination);
                stats.incrementTxPackets(1);
                stats.incrementTxBytes(packet.getPacketLength());
            } catch (Exception e) {
                stats.incrementTxErrors();
            }
        }

        private void pollingLoop() {
            ByteBuffer buffer = ByteBuffer.allocate(2048);

            while (pollingEnabled && !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.clear();
                    SocketAddress sender = channel.receive(buffer);

                    if (sender != null) {
                        buffer.flip();

                        // Create packet buffer
                        PacketBuffer packet = memoryPool.allocate();
                        if (packet != null) {
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);
                            packet.setData(data, 0, data.length);
                            packet.setTimestamp(System.nanoTime());

                            // Distribute to RX queue (simple round-robin)
                            int queueId = (int) (stats.getRxPackets().get() % receiveQueues.length);
                            if (receiveQueues[queueId].enqueuePacket(packet)) {
                                stats.incrementRxPackets(1);
                                stats.incrementRxBytes(data.length);
                            } else {
                                stats.incrementRxDropped();
                                memoryPool.deallocate(packet);
                            }
                        } else {
                            stats.incrementRxDropped();
                        }
                    }

                    // Small yield to prevent 100% CPU usage
                    if (sender == null) {
                        Thread.yield();
                    }

                } catch (Exception e) {
                    if (pollingEnabled) {
                        stats.incrementRxErrors();
                    }
                }
            }
        }

        // Configuration and management methods
        public boolean setPromiscuousMode(boolean enabled) {
            config.setPromiscuousMode(enabled);
            return true;
        }

        public boolean setMtu(int mtu) {
            if (mtu < 64 || mtu > 9000) {
                return false;
            }
            config.setMtu(mtu);
            return true;
        }

        public void setMacAddress(byte[] macAddress) {
            config.setMacAddress(macAddress);
        }

        public byte[] getMacAddress() {
            return config.getMacAddress();
        }

        public void resetStats() {
            stats.reset();
        }

        // Queue information
        public int getRxQueueCount() {
            return receiveQueues != null ? receiveQueues.length : 0;
        }

        public int getTxQueueCount() {
            return transmitQueues != null ? transmitQueues.length : 0;
        }

        public PacketQueue getRxQueue(int queueId) {
            return (receiveQueues != null && queueId < receiveQueues.length) ? receiveQueues[queueId] : null;
        }

        public PacketQueue getTxQueue(int queueId) {
            return (transmitQueues != null && queueId < transmitQueues.length) ? transmitQueues[queueId] : null;
        }

        // Utility method for creating test packets
        public PacketBuffer createTestPacket(String data) {
            PacketBuffer packet = memoryPool.allocate();
            if (packet != null) {
                byte[] bytes = data.getBytes();
                packet.setData(bytes, 0, bytes.length);
            }
            return packet;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UserSpacePacketDriverLLD solution = new UserSpacePacketDriverLLD();

        // Create and configure PMD device
        UserspacePacketDriver pmd = solution.new UserspacePacketDriver(0, "test-pmd");

        // Attach and configure device
        if (!pmd.attach()) {
            System.err.println("Failed to attach device");
            return;
        }

        DeviceConfig config = solution.new DeviceConfig();
        config.setMaxReceiveQueues(2);
        config.setMaxTransmitQueues(2);
        config.setPromiscuousMode(true);
        config.setChecksumOffload(true);

        if (!pmd.configure(config)) {
            System.err.println("Failed to configure device");
            return;
        }

        // Start device
        if (!pmd.start()) {
            System.err.println("Failed to start device");
            return;
        }

        System.out.println("PMD device started successfully");
        System.out.println("Device: " + pmd.getDeviceName());
        System.out.println("RX Queues: " + pmd.getRxQueueCount());
        System.out.println("TX Queues: " + pmd.getTxQueueCount());

        // Test packet transmission
        PacketBuffer[] transmitPackets = new PacketBuffer[5];
        IntStream.range(0, 5)
                .forEach(i -> transmitPackets[i] = pmd.createTestPacket("Test packet " + i));

        int transmitted = pmd.transmitPackets(0, transmitPackets, 5);
        System.out.println("Transmitted " + transmitted + " packets");

        // Test packet reception (simulation)
        PacketBuffer[] receivePackets = new PacketBuffer[10];
        Thread.sleep(100); // Allow some time for processing

        int received = pmd.receivePackets(0, receivePackets, 10);
        System.out.println("Received " + received + " packets");

        // Print statistics
        System.out.println("Statistics: " + pmd.getStats());

        // Cleanup
        Stream.of(transmitPackets)
                .filter(Objects::nonNull)
                .forEach(packet-> pmd.getMemoryPool().deallocate(packet));
        Arrays.stream(receivePackets, 0, received)
                .forEach(packet -> pmd.getMemoryPool().deallocate(packet));

        // Stop and detach
        pmd.stop();
        pmd.detach();

        System.out.println("PMD device stopped and detached");
    }
}

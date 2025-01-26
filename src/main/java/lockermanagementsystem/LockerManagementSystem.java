package lockermanagementsystem;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LockerManagementSystem {

    class Locker {
        private final Integer id;
        private final List<Slot> slots = new CopyOnWriteArrayList<>();

        public Locker(Integer id, Map<Size, Integer> slots) {
            this.id = id;
            this.slots.addAll(
                    slots
                            .entrySet()
                            .stream()
                            .flatMap(entry ->
                                             IntStream.range(0, entry.getValue())
                                                     .mapToObj(i -> new Slot(i, this, entry.getKey())))
                    .toList());
        }

        public void addSlot(Slot slot) {
            this.slots.add(slot);
        }

        Stream<Slot> getSlots() {
            return slots.stream();
        }

        Stream<Slot> getAvailableSlots() {
            return slots
                    .stream()
                    .filter(slot -> slot.assignedTo == null);
        }

        Stream<Slot> getAvailableSlots(Size size) {
            return getAvailableSlots()
                    .filter(slot -> size.equals(slot.size));
        }

        @Override
        public String toString() {
            return "Locker{" +
                    "id=" + id +
                    '}';
        }
    }

    class Slot {
        final int id;
        final Locker locker;
        final Size size;

        private volatile String otp;
        private volatile String assignedTo;
        private volatile Date assignedAt;

        private final Lock lock = new ReentrantLock();

        Slot(int id, Locker locker, Size size) {
            this.id = id;
            this.locker = locker;
            this.size = size;
        }

        public Slot assign(String assignedTo, String otp) {
            if (this.assignedTo != null)
                throw new InvalidRequestStateException("slot already assigned to someone else");
            try {
                lock.lock();
                if (this.assignedTo != null)
                    throw new InvalidRequestStateException("slot already assigned to someone else");
                this.assignedTo = assignedTo;
                this.assignedAt = new Date();
                this.otp = otp;
                return this;
            } finally {
                lock.unlock();
            }
        }

        public void vacate(String otp) {
            this.unlock(otp);
            this.vacate();
        }

        private void unlock(String otp) {
            if (this.assignedTo == null)
                throw new InvalidRequestStateException("Empty slot");
            if (!this.otp.equals(otp))
                throw new IllegalArgumentException("invalid otp");
            System.out.println("Unlocked slot " + id);
        }

        private void vacate() {
            try {
                lock.lock();
                this.assignedAt = null;
                this.assignedTo = null;
                this.otp = null;
            } finally {
                lock.unlock();
            }
            System.out.println("Vacated slot " + id);
        }

        public void vacate(long ttlInMilliseconds) {
            if (this.assignedTo == null || System.currentTimeMillis() - assignedAt.getTime() < ttlInMilliseconds)
                return;
            this.vacate();
        }

        public String getOtp() {
            return otp;
        }

        @Override
        public String toString() {
            return "Slot{" +
                    "id=" + id +
                    ", locker=" + locker +
                    ", size=" + size +
                    ", otp='" + otp + '\'' +
                    ", assignedTo='" + assignedTo + '\'' +
                    ", assignedAt=" + assignedAt +
                    '}';
        }
    }

    class OTPService {
        private final Random random = new Random();

        String createOTP() {
            return IntStream.range(0, 8)
                    .map(i -> random.nextInt(9))
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        }
    }

    enum Size {
        ONE
    }

    interface ISlotSelectionStrategy {
        Slot getAvailableSlot(Locker locker, Size size);
    }

    class RandomSelectionSlotSelectionStrategy implements ISlotSelectionStrategy {

        private final Random random = new Random();

        @Override
        public Slot getAvailableSlot(Locker locker, Size size) {
            List<Slot> slots =  locker.getAvailableSlots(size).toList();

            if (slots.isEmpty())
                throw new InvalidRequestStateException("No Slot available in the locker");

            int offset = random.nextInt(slots.size());
            return slots.get(offset);
        }
    }

    enum SlotSelectionStrategy {
        RANDOM,
        FIRST_AVAILABLE;
    }

    class LockerService {
        private final AtomicInteger counter = new AtomicInteger();
        private final Map<Integer, Locker> lockers = new ConcurrentHashMap<>();

        private final Map<SlotSelectionStrategy, ISlotSelectionStrategy> slotSelectionStrategies = new ConcurrentHashMap<>();

        private final OTPService otpService;

        public LockerService(final OTPService otpService) {
            this.otpService = otpService;
            this.slotSelectionStrategies.put(SlotSelectionStrategy.RANDOM, new RandomSelectionSlotSelectionStrategy());
        }

        public Stream<Locker> getLockers() {
            return lockers.values().stream();
        }

        public Locker createLocker(Map<Size, Integer> slots) {
            Integer id = counter.incrementAndGet();
            Locker locker = new Locker(id, slots);
            lockers.put(id, new Locker(id, slots));
            return locker;
        }

        public Slot getSlot(String assignTo, Integer lockerId, Size size, SlotSelectionStrategy slotSelectionStrategy) {
            final Locker locker = lockers.get(lockerId);
            if (locker == null)
                throw new IllegalArgumentException("No locker found with id: " + lockerId);
            while (true) {
                try {
                    Slot slot = this.slotSelectionStrategies.get(slotSelectionStrategy)
                            .getAvailableSlot(locker, size);
                    slot.assign(assignTo, otpService.createOTP());
                    return slot;
                } catch (InvalidRequestStateException invalidRequestStateException) {
                }
            }
        }
    }

    class Admin {

        private final LockerService lockerService;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        public Admin(final LockerService lockerService) {
            this.lockerService = lockerService;
            this.executeVacate();
        }

        public void executeVacate() {
            scheduler.scheduleAtFixedRate(this::vacate, 0, 3, TimeUnit.DAYS);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (scheduler != null)
                    while (!scheduler.isShutdown())
                        scheduler.shutdown();
            }));
        }

        public void vacate() {
            this.lockerService
                    .getLockers()
                    .flatMap(Locker::getSlots)
                    .forEach(slot -> slot.vacate(3 * 86400 * 1000));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final LockerManagementSystem system = new LockerManagementSystem();
        final OTPService otpService = system.new OTPService();
        final LockerService lockerService = system.new LockerService(otpService);
        final Locker locker = lockerService.createLocker(new HashMap<>() {{
            put(Size.ONE, 10);
        }});
        final Runnable task = () -> {
            Slot slot = lockerService.getSlot("A", locker.id, Size.ONE, SlotSelectionStrategy.RANDOM);
            System.out.println(slot);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            slot.vacate(slot.getOtp());
            System.out.println(slot);
        };
        int c = 2;
        while (c-- > 0) {
            IntStream.range(0, 10)
//                .forEach(i -> task.run());
                .mapToObj(i -> new Thread(task))
                .forEach(Thread::start);
            Thread.sleep(900);
        }
    }
}

package loggerlibrary.loggers.implementations;

import loggerlibrary.api.Message;
import loggerlibrary.configurations.LoggerConfiguration;
import loggerlibrary.sinks.ISink;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;

public class AsynchronousLogger extends SimpleLogger {

    private final Queue<Message> messages = new PriorityBlockingQueue<>(11,
            (a, b) -> a.getDate().compareTo(b.getDate()));
    private final ExecutorService[] logSinkScheduler = new ExecutorService[10];
    private final Thread messageProcessor;

    public AsynchronousLogger(final LoggerConfiguration loggerConfiguration) {
        super(loggerConfiguration);
        IntStream.range(0, 10)
                .forEach(i -> this.logSinkScheduler[i] = Executors.newSingleThreadExecutor());
        this.messageProcessor = new Thread(this::poll);
        this.messageProcessor.start();
        this.registerShutdownHook();
    }

    @Override
    public void log(final Message message) {
        message.setDate(new Date());
        synchronized (this.messages) {
            this.messages.offer(message);
            this.messages.notifyAll();
        }
    }

    protected void sink(final Message message, final ISink sink) {
        this.logSinkScheduler[sink.getId() % logSinkScheduler.length]
                .execute(() -> sink.writeMessage(message));
    }

    private Message poll() {
        while (true) {
            synchronized (this.messages) {
                if (!this.messages.isEmpty()) {
                    Message message = this.messages.poll();
                    this.log(message.getLogLevel(), message);
                } else {
                    try {
                        this.messages.wait();
                    } catch (final InterruptedException iex) {
                        iex.printStackTrace();
                    }
                }
            }
        }
    }

    private final void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            while (!this.messages.isEmpty()) {
                this.shutdownHookSleep();
            }
            if (Thread.State.WAITING.equals(this.messageProcessor.getState())) {
                this.messageProcessor.stop();
            }
            for (ExecutorService logSinkScheduler : this.logSinkScheduler) {
                logSinkScheduler.shutdown();
                while (!logSinkScheduler.isTerminated()) {
                    this.shutdownHookSleep();
                }
            }
        }));
    }

    private final void shutdownHookSleep() {
        try {
            Thread.sleep(10000);
        } catch (final InterruptedException iex) {
            iex.printStackTrace();
        }
    }

}

package logger;

public class FileLoggerAdapter implements ILogger {

    private FileLogger fileLogger;


    public FileLoggerAdapter(final FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }

    @Override
    public void log(String message) {
        this.fileLogger.writeToFile(message);
    }
}

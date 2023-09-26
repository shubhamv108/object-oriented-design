package logger;

public class Main {

    /**
     * Adapter (Structural):
     * 1. Integrating Thid Party Api and Libbriaries
     * 2. Legacy System Interoperability
     * 3. Interface Standardization in Large Systems
     * @param args
     */
    public static void main(String[] args) {
        SerialLogger serialLogger = new SerialLogger();
        Log4jLogger log4jLogger = new Log4jLogger();
        FileLogger fileLogger = new FileLogger();

        ILogger logger1 = new SerialLogAdapter();
        ILogger logger2 = new Log4JAdapter();
        ILogger logger3 = new FileLoggerAdapter(fileLogger);

    }
}

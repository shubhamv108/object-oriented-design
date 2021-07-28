package logginglibrary.constants;

public enum LogLevel {

    FATAl(1), ERROR(2), WARN(3), INFO(4), DEBUG(5);

    private int level;

    LogLevel(final int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public LogLevel getLowerLevel(final LogLevel logLevel) {
        int lowerLevel = logLevel.getLevel() + 1;
        for (LogLevel level : LogLevel.values()) {
            if (level.getLevel() == lowerLevel) {
                return level;
            }
        }
        return null;
    }
}

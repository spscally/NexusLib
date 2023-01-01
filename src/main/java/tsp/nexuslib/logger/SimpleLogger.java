package tsp.nexuslib.logger;

/**
 * A simple logger that can be implemented to fit any way of logging.
 *
 * @author TheSilentPro (Silent)
 */
public interface SimpleLogger {

    default String getName() {
        return "LOG";
    }

    default boolean isDebug() {
        return false;
    }

    default void info(String message) {
        this.log(LogLevel.INFO, message);
    }

    default void warning(String message) {
        this.log(LogLevel.WARNING, message);
    }

    default void error(String message) {
        this.log(LogLevel.ERROR, message);
    }

    default void debug(String message) {
        this.log(LogLevel.DEBUG, message);
    }

    default void trace(String message) {
        this.log(LogLevel.TRACE, message);
    }

    void log(LogLevel level, String message);

    enum LogLevel {

        INFO,
        WARNING,
        ERROR,
        DEBUG,
        TRACE;

        /**
         * Get the text color of the level
         *
         * @return The color
         */
        public String getColor() {
            return switch (this) {
                case INFO -> "\u001B[32m"; // Green
                case WARNING -> "\u001B[33m"; // Yellow
                case ERROR -> "\u001B[31m"; // Red
                case DEBUG -> "\u001B[36m"; // Cyan
                case TRACE -> "\u001b[35m"; // Purple
            };
        }

    }

}

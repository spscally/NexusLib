package tsp.nexuslib.logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLogger;

import java.util.logging.Level;

public class NexusLogger extends PluginLogger implements SimpleLogger {

    private boolean debug;

    public NexusLogger(Plugin plugin, boolean debug) {
        super(plugin);
        this.debug = debug;
    }

    public NexusLogger(Plugin plugin) {
        this(plugin, false);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public void log(Level level, String msg) {
        if (!debug && (level == Level.FINE || level == Level.FINER || level == Level.FINEST || level == Level.CONFIG)) {
            return;
        }

        super.log(level, msg);
    }

    @Override
    public void log(LogLevel level, String message) {
        this.log(mapToLevel(level), message);
    }

    private Level mapToLevel(LogLevel logLevel) {
        return switch (logLevel) {
            case INFO -> Level.INFO;
            case WARNING -> Level.WARNING;
            case ERROR -> Level.SEVERE;
            case DEBUG -> Level.FINE;
            case TRACE -> Level.FINER;
        };
    }

}

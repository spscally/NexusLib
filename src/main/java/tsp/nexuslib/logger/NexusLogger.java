package tsp.nexuslib.logger;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NexusLogger extends Logger {

    private final boolean debug;

    public NexusLogger(boolean debug) {
        super("ArcaneEnchantments", null);
        this.debug = debug;
        setParent(Bukkit.getLogger());
    }

    public void warn(String message) {
        log(Level.WARNING, message);
    }

    public void error(String message) {
        log(Level.SEVERE, message);
    }

    public void debug(String message) {
        if (debug) {
            log(Level.INFO, /*"\\e[0;36m" +*/ "[DEBUG]: " + message);
        }
    }

    public void trace(String message) {
        log(Level.FINER, message);
    }

}

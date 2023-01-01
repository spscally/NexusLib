package tsp.nexuslib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tsp.nexuslib.NexusPlugin;

public class BukkitListener implements Listener {

    public BukkitListener(JavaPlugin plugin) {
        if (autoRegister()) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }

    public BukkitListener() {
        this(NexusPlugin.getInstance());
    }

    public boolean autoRegister() {
        return this instanceof AutoRegister;
    }

}

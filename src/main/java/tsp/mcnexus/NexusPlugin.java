package tsp.mcnexus;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class NexusPlugin extends JavaPlugin {

    private static NexusPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        onStart(instance);
    }

    public abstract void onStart(NexusPlugin instance);

    public static NexusPlugin getInstance() {
        return instance;
    }

}

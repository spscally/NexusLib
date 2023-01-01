package tsp.nexuslib.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listens for click events for the {@link PagedPane} and {@link Pane}
 * Requires to be registered.
 */
public class PaneListener implements Listener {

    public PaneListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Pane) {
            ((Pane) holder).onClick(event);
        }
    }
}
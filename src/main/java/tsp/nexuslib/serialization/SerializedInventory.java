package tsp.nexuslib.serialization;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;

public record SerializedInventory(ItemStack[] items) {

    public SerializedInventory of(@Nonnull Inventory inventory) {
        Validate.notNull(inventory, "Inventory can not be null!");

        return new SerializedInventory(inventory.getContents());
    }

}
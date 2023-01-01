package tsp.nexuslib.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Utility class for managing inventories
 *
 * @author TheSilentPro
 */
public final class InventoryUtils {

    private InventoryUtils() {}

    /**
     * Fill the borders of an {@link Inventory}
     *
     * @param inv The target {@link Inventory}
     * @param item Item to use for filling
     */
    public static void fillBorder(Inventory inv, ItemStack item) {
        int size = inv.getSize();
        int rows = (size + 1) / 9;

        // Fill top
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }

        // If inventory is only one row, no need for anything else
        if (size > 9) {
            // Fill bottom
            for (int i = size - 9; i < size; i++) {
                inv.setItem(i, item);
            }

            // Fill sides
            for (int i = 2; i <= rows - 1; i++) {
                inv.setItem(i * 9 - 1, item);
                inv.setItem((i - 1) * 9, item);
            }
        }
    }

    /**
     * Fills a row in an {@link Inventory}
     *
     * @param rowIndex Index of the row to fill (0 - 6)
     * @param itemStack The {@link ItemStack} to use for filling
     * @param inventory The target {@link Inventory}
     * @param onlyEmpty If only empty slots should be filled
     */
    public static void fillRow(Inventory inventory, int rowIndex, ItemStack itemStack, boolean onlyEmpty) {
        int x = rowIndex * 9;
        for (int i = 0; i < 9; i++) {
            int slot = x + i;

            if (!onlyEmpty) {
                inventory.setItem(slot, itemStack);
            } else {
                ItemStack slotItem = inventory.getItem(i);
                if (slotItem == null || slotItem.getType().isAir()) {
                    inventory.setItem(slot, itemStack);
                }
            }
        }
    }

    /**
     * Fills the empty slots of an inventory
     *
     * @param inventory The target {@link Inventory}
     * @param item The {@link ItemStack} used for filling empty slots
     */
    public static void fill(Inventory inventory, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack slotItem = inventory.getItem(i);
            if (slotItem == null || slotItem.getType().isAir()) {
                inventory.setItem(i, item);
            }
        }
    }

    /**
     * Fills the empty slots of an {@link Inventory}
     *
     * @param inv The inventory to fill
     * @param item The item used for filling
     * @param ignored Ignored slots. Must be ordered lowest -> highest
     */
    public static void binaryFill(Inventory inv, ItemStack item, int... ignored) {
        int size = inv.getSize();

        // Fill
        for (int i = 0; i < size; i++) {
            // Check if slot is not ignored
            if (ignored != null && Arrays.binarySearch(ignored, i) < 0) {
                ItemStack slotItem = inv.getItem(i);
                if (slotItem == null || slotItem.getType().isAir()) {
                    inv.setItem(i, item);
                }
            }
        }
    }

}

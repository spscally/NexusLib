package tsp.nexuslib.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Page {

    private final Map<Integer, Button> buttons = new HashMap<>();
    private final int maxSize;

    public Page(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @param event The click event
     */
    public void handleClick(@Nonnull InventoryClickEvent event) {
        Validate.notNull(event, "InventoryClickEvent must not be null!");

        // user clicked in his own tsp.smartaddon.inventory. Silently drop it
        if (event.getRawSlot() > event.getInventory().getSize()) {
            return;
        }
        // user clicked outside of the tsp.smartaddon.inventory
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        if (event.getSlot() >= buttons.size()) {
            return;
        }
        Button button = buttons.get(event.getSlot());
        if (button != null) {
            button.onClick(event);
        }
    }

    /**
     * @return True if there is space left
     */
    public boolean hasSpace() {
        return buttons.size() < maxSize * 9;
    }

    /**
     * @param button The {@link Button} to add
     *
     * @return True if the button was added, false if there was no space
     */
    public boolean addButton(@Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        if (!hasSpace()) {
            return false;
        }
        buttons.put(buttons.size(), button);
        return true;
    }

    /**
     * @param i Slot
     * @param button The {@link Button} to add
     * @return True if the button was added
     */
    public boolean setButton(int i, @Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        buttons.put(i, button);
        return true;
    }

    /**
     * @param button The {@link Button} to remove
     *
     * @return True if the button was removed
     */
    public boolean removeButton(@Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        Map.Entry<Integer, Button> match = null;
        for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
            if (entry.getValue().equals(button)) {
                match = entry;
                break;
            }
        }

        return match != null && buttons.remove(match.getKey(), match.getValue());
    }

    /**
     * @param inventory The inventory to render in
     */
    public void render(@Nonnull Inventory inventory) {
        Validate.notNull(inventory, "Inventory must not be null!");

        for (Map.Entry<Integer, Button> button : buttons.entrySet()) {
            inventory.setItem(button.getKey(), button.getValue().getItemStack());
        }
    }

    /**
     * @return True if this page is empty
     */
    public boolean isEmpty() {
        return buttons.isEmpty();
    }
}
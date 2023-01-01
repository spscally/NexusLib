package tsp.nexuslib.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * A paged pane.
 *
 * @author I Al Ianstaan
 * @author TheSilentPro
 */
public class PagedPane extends Pane {

    private final Inventory inventory;

    private final SortedMap<Integer, Page> pages = new TreeMap<>();
    private int currentIndex;
    private final int pageSize;
    private ItemStack borderItem;
    private ItemStack backItem;
    private ItemStack nextItem;
    private ItemStack currentItem;

    private Button controlBack;
    private Button controlNext;
    private Button controlCurrent;

    /**
     * @param pageSize The page size
     */
    public PagedPane(int pageSize, int rows, @Nonnull String title) {
        super(rows, title);
        if (rows > 6) {
            throw new IllegalArgumentException("Rows must be <= 6, got " + rows);
        }
        if (pageSize > 6) {
            throw new IllegalArgumentException("Page size must be <= 6, got" + pageSize);
        }

        this.borderItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        this.backItem = new ItemStack(Material.ARROW);
        this.nextItem = new ItemStack(Material.ARROW);
        this.currentItem = new ItemStack(Material.BOOK);

        this.pageSize = pageSize;
        inventory = Bukkit.createInventory(this, rows * 9, color(title));

        pages.put(0, new Page(pageSize));
    }

    /**
     * @param button The button to add
     */
    public void addButton(@Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().addButton(button)) {
                if (entry.getKey() == currentIndex) {
                    reRender();
                }
                return;
            }
        }
        Page page = new Page(pageSize);
        page.addButton(button);
        pages.put(pages.lastKey() + 1, page);

        reRender();
    }

    public void setButton(int i, @Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().setButton(i, button)) {
                if (entry.getKey() == currentIndex) {
                    reRender();
                }
                return;
            }
        }
        Page page = new Page(pageSize);
        page.setButton(i, button);
        pages.put(pages.lastKey() + 1, page);

        reRender();
    }

    /**
     * @param button The Button to remove
     */
    @SuppressWarnings("unused")
    public void removeButton(@Nonnull Button button) {
        Validate.notNull(button, "Button must not be null!");

        for (Iterator<Map.Entry<Integer, Page>> iterator = pages.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Integer, Page> entry = iterator.next();
            if (entry.getValue().removeButton(button)) {

                // we may need to delete the page
                if (entry.getValue().isEmpty()) {
                    // we have more than one page, so delete it
                    if (pages.size() > 1) {
                        iterator.remove();
                    }
                    // the currentIndex now points to a page that does not exist. Correct it.
                    if (currentIndex >= pages.size()) {
                        currentIndex--;
                    }
                }
                // if we modified the current one, re-render
                // if we deleted the current page, re-render too
                if (entry.getKey() >= currentIndex) {
                    reRender();
                }
                return;
            }
        }
    }

    /**
     * @return The amount of pages
     */
    @SuppressWarnings("WeakerAccess")
    public int getPageAmount() {
        return pages.size();
    }

    /**
     * @return The number of the current page (1 based)
     */
    @SuppressWarnings("WeakerAccess")
    public int getCurrentPage() {
        return currentIndex + 1;
    }

    /**
     * @param index The index of the new page
     */
    @SuppressWarnings("WeakerAccess")
    public void selectPage(int index) {
        if (index < 0 || index >= getPageAmount()) {
            throw new IllegalArgumentException(
                    "Index out of bounds s: " + index + " [" + 0 + " " + getPageAmount() + ")"
            );
        }
        if (index == currentIndex) {
            return;
        }

        currentIndex = index;
        reRender();
    }

    /**
     * Renders the inventory again
     */
    @SuppressWarnings("WeakerAccess")
    public void reRender() {
        inventory.clear();
        pages.get(currentIndex).render(inventory);

        controlBack = null;
        controlNext = null;
        createControls(inventory);
    }

    /**
     * @param event The {@link InventoryClickEvent}
     */
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getRawSlot() == inventory.getSize() - 8) {
            if (controlBack != null) {
                controlBack.onClick(event);
            }
        } else if (event.getRawSlot() == inventory.getSize() - 2) {
            if (controlNext != null) {
                controlNext.onClick(event);
            }
        } else if (event.getRawSlot() == inventory.getSize() - 5) {
            if (controlCurrent != null) {
                controlCurrent.onClick(event);
            }
        } else {
            pages.get(currentIndex).handleClick(event);
        }
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Nonnull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public SortedMap<Integer, Page> getPages() {
        return pages;
    }

    public Page getPage(int index) {
        return pages.get(index);
    }

    /**
     * Creates the controls
     *
     * @param inventory The inventory
     */
    private void createControls(Inventory inventory) {
        // create separator
        fillRow(
                inventory.getSize() / 9 - 2,
                borderItem,
                inventory
        );

        if (getCurrentPage() > 1) {
            String name = String.format(
                    Locale.ROOT,
                    "&3&lPage &a&l%d &7/ &c&l%d",
                    getCurrentPage() - 1, getPageAmount()
            );
            String lore = String.format(
                    Locale.ROOT,
                    "&7Brings you back to the page &c%d",
                    getCurrentPage() - 1
            );
            ItemStack itemStack = getItemStack(backItem, name, lore);
            controlBack = new Button(itemStack, event -> selectPage(currentIndex - 1));
            inventory.setItem(inventory.getSize() - 8, itemStack);
        }

        if (getCurrentPage() < getPageAmount()) {
            String name = String.format(
                    Locale.ROOT,
                    "&3&lPage &a&l%d &7/ &c&l%d",
                    getCurrentPage() + 1, getPageAmount()
            );
            String lore = String.format(
                    Locale.ROOT,
                    "&7Brings you to the page &c%d",
                    getCurrentPage() + 1
            );
            ItemStack itemStack = getItemStack(nextItem, name, lore);
            controlNext = new Button(itemStack, event -> selectPage(getCurrentPage()));
            inventory.setItem(inventory.getSize() - 2, itemStack);
        }

        {
            String name = String.format(
                    Locale.ROOT,
                    "&3&lPage &a&l%d &7/ &c&l%d",
                    getCurrentPage(), getPageAmount()
            );
            String lore = String.format(
                    Locale.ROOT,
                    "&7You are on page &a%d &7/ &c%d",
                    getCurrentPage(), getPageAmount()
            );
            ItemStack itemStack = getItemStack(currentItem, name, lore);
            inventory.setItem(inventory.getSize() - 5, itemStack);
        }
    }

    private void fillRow(int rowIndex, @Nonnull ItemStack itemStack, @Nonnull Inventory inventory) {
        int yMod = rowIndex * 9;
        for (int i = 0; i < 9; i++) {
            int slot = yMod + i;
            inventory.setItem(slot, itemStack);
        }
    }

    /**
     * @param item The item.
     * @param name The name. May be null.
     * @param lore The lore. May be null.
     *
     * @return The item
     */
    @SuppressWarnings("ConstantConditions")
    private ItemStack getItemStack(@Nonnull ItemStack item, @Nullable String name, @Nullable String... lore) {
        Validate.notNull(item, "Item must not be null!");

        ItemMeta itemMeta = item.getItemMeta();
        if (name != null) {
            itemMeta.setDisplayName(color(name));
        }
        if (lore != null && lore.length != 0) {
            itemMeta.setLore(Arrays.stream(lore).map(this::color).collect(Collectors.toList()));
        }
        item.setItemMeta(itemMeta);

        return item;
    }

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * @param player The {@link Player} to open it for
     */
    public void open(Player player) {
        reRender();
        player.openInventory(getInventory());
    }

    /**
     * Sets the item for the border seperator
     *
     * @param item Border item
     */
    public void setBorderItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "Item must not be null!");

        this.borderItem = item;
    }

    public void setControlCurrent(Button controlCurrent) {
        this.controlCurrent = controlCurrent;
    }

    /**
     * Sets the "current page" item
     *
     * @param item Current Item
     */
    public void setCurrentItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "Item must not be null!");

        this.currentItem = item;
    }

    /**
     * Sets the previous page item
     *
     * @param item Back item
     */
    public void setBackItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "Item must not be null!");

        this.backItem = item;
    }

    /**
     * Sets the next page item
     *
     * @param item Next item
     */
    public void setNextItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "Item must not be null!");

        this.nextItem = item;
    }

    public ItemStack getBorderItem() {
        return borderItem;
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }

    public ItemStack getBackItem() {
        return backItem;
    }

    public ItemStack getNextItem() {
        return nextItem;
    }

}
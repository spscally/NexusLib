package tsp.mcnexus.builder.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder {

    private final LeatherArmorMeta armorMeta;

    public LeatherArmorBuilder(ItemStack item) {
        super(item);
        armorMeta = (LeatherArmorMeta) item.getItemMeta();
    }

    public LeatherArmorBuilder() {
        this(new ItemStack(Material.LEATHER_CHESTPLATE));
    }

    public LeatherArmorBuilder color(Color color) {
        armorMeta.setColor(color);
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(armorMeta);
        return super.build();
    }

}

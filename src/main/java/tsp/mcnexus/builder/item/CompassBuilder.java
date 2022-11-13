package tsp.mcnexus.builder.item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import javax.annotation.Nonnull;

public class CompassBuilder extends ItemBuilder {

    private final CompassMeta compassMeta;

    public CompassBuilder(@Nonnull ItemStack item) {
        super(item);
        compassMeta = (CompassMeta) item.getItemMeta();
    }

    public CompassBuilder() {
        this(new ItemStack(Material.COMPASS));
    }

    public CompassBuilder lodestone(Location location) {
        compassMeta.setLodestone(location);
        return this;
    }

    public CompassBuilder tracked(boolean b) {
        compassMeta.setLodestoneTracked(b);
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(compassMeta);
        return super.build();
    }
}

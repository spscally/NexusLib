package tsp.mcnexus.builder.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BundleBuilder extends ItemBuilder {

    private final BundleMeta bundleMeta;

    public BundleBuilder(@Nonnull ItemStack item) {
        super(item);
        bundleMeta = (BundleMeta) item.getItemMeta();
    }

    public BundleBuilder() {
        this(new ItemStack(Material.BUNDLE));
    }

    public BundleBuilder addItems(Collection<ItemStack> items) {
        for (ItemStack item : items) {
            bundleMeta.addItem(item);
        }
        return this;
    }

    public BundleBuilder addItems(ItemStack... items) {
        for (ItemStack item : items) {
            bundleMeta.addItem(item);
        }
        return this;
    }

    public BundleBuilder setItems(List<ItemStack> items) {
        bundleMeta.setItems(items);
        return this;
    }

    public BundleBuilder setItems(ItemStack... items) {
        bundleMeta.setItems(Arrays.asList(items));
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(bundleMeta);
        return super.build();
    }

}

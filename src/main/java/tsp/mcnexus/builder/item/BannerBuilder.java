package tsp.mcnexus.builder.item;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class BannerBuilder extends ItemBuilder {

    private final BannerMeta bannerMeta;

    public BannerBuilder(ItemStack item) {
        super(item);
        bannerMeta = (BannerMeta) item.getItemMeta();
    }

    public BannerBuilder() {
        this(new ItemStack(Material.WHITE_BANNER));
    }

    public BannerBuilder addPatterns(Pattern... patterns) {
        for (Pattern pattern : patterns) {
            bannerMeta.addPattern(pattern);
        }
        return this;
    }

    public BannerBuilder setPatterns(List<Pattern> patterns) {
        bannerMeta.setPatterns(patterns);
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(bannerMeta);
        return super.build();
    }

}

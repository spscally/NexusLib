package tsp.mcnexus.builder.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.Map;

public class PotionBuilder extends ItemBuilder {

    private final PotionMeta potionMeta;

    public PotionBuilder(@Nonnull ItemStack item) {
        super(item);
        potionMeta = (PotionMeta) item.getItemMeta();
    }

    public PotionBuilder() {
        this(new ItemStack(Material.POTION));
    }

    public PotionBuilder color(Color color) {
        potionMeta.setColor(color);
        return this;
    }

    public PotionBuilder basePotionData(PotionData potionData) {
        potionMeta.setBasePotionData(potionData);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PotionBuilder addEffect(PotionEffect effect, boolean overwrite) {
        potionMeta.addCustomEffect(effect, overwrite);
        return this;
    }

    public PotionBuilder addEffect(PotionEffect effect) {
        potionMeta.addCustomEffect(effect, true);
        return this;
    }

    public PotionBuilder addEffects(boolean overwrite, PotionEffect... effects) {
        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, overwrite);
        }
        return this;
    }

    public PotionBuilder addEffects(PotionEffect... effects) {
        return addEffects(true, effects);
    }

    public PotionBuilder addEffects(Map<PotionEffect, Boolean> effects) {
        for (Map.Entry<PotionEffect, Boolean> entry : effects.entrySet()) {
            addEffect(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(potionMeta);
        return super.build();
    }

}

package tsp.mcnexus.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionUtils {

    /**
     * Retrieve a {@link PotionEffect} with translated (human) values
     *
     * @param type The type
     * @param duration The duration in seconds
     * @param amplifier The amplifier starting from 1
     * @param ambient Ambient
     * @param particles Particles
     * @return PotionEffect
     */
    public static PotionEffect translate(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        return new PotionEffect(type, duration * 20, amplifier - 1, ambient, particles);
    }

    public static PotionEffect translate(PotionEffectType type, int duration, int amplifier) {
        return translate(type, duration, amplifier, false, false);
    }

}

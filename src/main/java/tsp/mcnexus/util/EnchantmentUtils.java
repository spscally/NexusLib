package tsp.mcnexus.util;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentUtils {

    /**
     * Translate all enchantments from a list to a {@link Map}.
     * <bold>FORMAT: <ENCHANTMENT:LEVEL></bold>
     *
     * @param list The list of enchantments.
     * @return Translated enchantments
     */
    public static Map<Enchantment, Integer> translateMap(List<String> list) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (String entry : list) {
            Pair<Enchantment, Integer> pair = translate(entry);
            enchantments.put(pair.getKey(), pair.getValue());
        }

        return enchantments;
    }

    /**
     * Translate an enchantment in string to a {@link Pair}.
     * <bold>FORMAT: <ENCHANTMENT:LEVEL></bold>
     *
     * @param string The string containing the enchantment.
     * @return Translated enchantment.
     */
    public static Pair<Enchantment, Integer> translate(String string) {
        String enchantment = string.substring(0, string.indexOf(":"));
        String level = string.substring(string.indexOf(":", string.length()));

        return new Pair<>(Enchantment.getByKey(NamespacedKey.minecraft(enchantment)), Integer.parseInt(level));
    }
}

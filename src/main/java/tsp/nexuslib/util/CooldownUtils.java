package tsp.nexuslib.util;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import tsp.nexuslib.NexusPlugin;
import tsp.nexuslib.persistence.PersistentDataAPI;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author TheSilentPro (Silent)
 */
public class CooldownUtils {

    public static final NamespacedKey COOLDOWN_KEY = new NamespacedKey(NexusPlugin.getInstance(), "cooldown");
    public static final NamespacedKey LAST_USED_KEY = new NamespacedKey(NexusPlugin.getInstance(), "lastused");


    /**
     * Add cooldown to an holder
     *
     * @param holder The holder to add a cooldown to
     * @param time The cooldown time
     */
    public static void setCooldown(@Nonnull PersistentDataHolder holder, long time) {
        if (time > -1) {
            PersistentDataAPI.setLong(holder, COOLDOWN_KEY, time);
            PersistentDataAPI.setLong(holder, LAST_USED_KEY, System.currentTimeMillis());
        }
    }

    /**
     * Get the cooldown time left on an holder
     *
     * @param holder The holder to check
     * @return Time left. Returns -1 if there is no cooldown
     */
    public static long getTimeLeft(@Nonnull PersistentDataHolder holder) {
        long l = PersistentDataAPI.getLong(holder, LAST_USED_KEY, -1);
        long time = PersistentDataAPI.getLong(holder, COOLDOWN_KEY, -1);
        if (l < 1 || time < 1) {
            return -1;
        }

        return ((l + time * 1000) - System.currentTimeMillis()) / 1000;
    }

    /**
     * Use an holder and set it on cooldown
     *
     * @param holder The holder to use
     * @param cooldown The cooldown for the holder when used
     * @param ready Ran when the holder is used. Long is the cooldown the holder was set to
     * @param timeleft Ran when the holder is not ready. Long is time left
     */
    public static void use(@Nonnull PersistentDataHolder holder, long cooldown, @Nonnull Consumer<Long> ready, @Nonnull Consumer<Long> timeleft) {
        long time = getTimeLeft(holder);
        if (time < 1) {
            ready.accept(cooldown);
            setCooldown(holder, cooldown);
        } else {
            timeleft.accept(time);
        }
    }

    /**
     * Use an holder and set it on cooldown
     *
     * @param holder The holder to use
     * @param cooldown The cooldown for the holder when used
     */
    public static void use(@Nonnull PersistentDataHolder holder, long cooldown) {
        long time = getTimeLeft(holder);
        if (time < 1) {
            setCooldown(holder, cooldown);
        }
    }
    
}

package tsp.nexuslib.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author TheSilentPro (Silent)
 */
@ParametersAreNonnullByDefault
public final class EntityUtils {

    private EntityUtils() {}

    public static void heal(Damageable target, double amount) {
        if (target instanceof Attributable attributable) {
            target.setHealth(Math.min(attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), target.getHealth() + amount));
        }
    }

    public static void kill(Damageable target, @Nullable Entity source) {
        if (source == null) {
            target.setHealth(0D);
        } else {
            target.damage(target.getHealth(), source);
        }
    }

    public static void kill(Damageable target) {
        kill(target, null);
    }

    public static Set<Entity> getAllEntities(Predicate<Entity> filter, boolean onlyLiving) {
        for (World world : Bukkit.getWorlds()) {
            if (onlyLiving) {
                return world.getLivingEntities()
                        .stream()
                        .filter(filter)
                        .collect(Collectors.toSet());
            } else {
                return world.getEntities()
                        .stream()
                        .filter(filter)
                        .collect(Collectors.toSet());
            }
        }

        return Collections.emptySet();
    }

    public static Set<Entity> getAllEntities(Predicate<Entity> filter) {
        return getAllEntities(filter, false);
    }

    public static Set<Entity> getNearbyEntities(Entity source, double x, double y, double z, Predicate<Entity> filter) {
        return source.getNearbyEntities(x, y, z)
                .stream()
                .filter(entity -> !entity.getUniqueId().equals(source.getUniqueId()))
                .filter(filter)
                .collect(Collectors.toSet());
    }

    public static Collection<Entity> getNearbyEntities(Entity source, double radius, Predicate<Entity> filter) {
        return getNearbyEntities(source, radius, radius, radius, filter);
    }

}
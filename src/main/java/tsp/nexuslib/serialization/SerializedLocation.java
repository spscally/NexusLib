package tsp.nexuslib.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 * Serialized {@link Location}.
 * @author TheSilentPro (Silent)
 *
 * @param uniqueId The unique id of the world.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param z The Z coordinate.
 * @param yaw The Yaw.
 * @param pitch The Pitch.
 */
public record SerializedLocation(@Nonnull UUID uniqueId, double x, double y, double z, @Nullable Float yaw, @Nullable Float pitch) implements Serializable {

    public SerializedLocation(@Nonnull UUID uniqueId, double x, double y, double z) {
        this(uniqueId, x, y, z, null, null);
    }

    public static SerializedLocation of(@Nonnull Location location) {
        Validate.notNull(location, "Location can not be null!");
        Validate.notNull(location.getWorld(), "World location can not be null!");

        return new SerializedLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public Optional<Location> toLocation() {
        World world = Bukkit.getWorld(uniqueId);
        if (world == null) {
            return Optional.empty();
        } else {
            if (yaw == null || pitch == null) {
                return Optional.of(new Location(world, x, y, z));
            } else {
                return Optional.of(new Location(world, x, y, z, yaw, pitch));
            }
        }
    }

    @Nonnull
    public Optional<Float> getYaw() {
        return Optional.ofNullable(yaw);
    }

    @Nonnull
    public Optional<Float> getPitch() {
        return Optional.ofNullable(pitch);
    }

}
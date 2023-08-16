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
import java.util.regex.Pattern;

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

    private static final Pattern DELIMITER = Pattern.compile(";");

    public SerializedLocation(@Nonnull UUID uniqueId, double x, double y, double z) {
        this(uniqueId, x, y, z, null, null);
    }

    /**
     * Convert from {@link Location}.
     *
     * @param location The location object
     * @return location
     */
    public static SerializedLocation of(@Nonnull Location location) {
        Validate.notNull(location, "Location can not be null!");
        Validate.notNull(location.getWorld(), "World location can not be null!");

        return new SerializedLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Convert from string.
     *
     * @param serializedLocation Location serialized with {@link #toString()}
     * @return location
     */
    public static SerializedLocation of(@Nonnull String serializedLocation) {
        Validate.notNull(serializedLocation, "Location can not be null!");

        String[] args = DELIMITER.split(serializedLocation);
        return new SerializedLocation(UUID.fromString(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), parseFloat(args[4]), parseFloat(args[5]));
    }

    /**
     * Convert current to {@link Location}.
     *
     * @return Optional location
     */
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

    /**
     * Serialize the location to a string.
     *
     * @return string representation of the location.
     */
    @Nonnull
    public String toString() {
        return uniqueId.toString() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

    private static Float parseFloat(String raw) {
        try {
            return Float.parseFloat(raw);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}

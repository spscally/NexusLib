package tsp.mcnexus.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Utilities for serializing objects
 *
 * @author TheSilentPro
 */
@Deprecated
@SuppressWarnings("ALL")
public final class SerializationUtils {

    private SerializationUtils() {}

    // Location
    /**
     * Serialize location in a {@link FileConfiguration} under a specific key
     *
     * @param location The location to serialize
     * @param data The file configuration
     * @param key The key to serialize the location under
     * @see #deserializeLocation(FileConfiguration, String)
     */
    public static void serializeLocation(Location location, FileConfiguration data, @Nonnull String key) {
        if (!key.isEmpty()) {
            key = key + ".";
        }

        String uid = location.getWorld().getUID().toString();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        data.set(key + "world", uid);
        data.set(key + "x", x);
        data.set(key + "y", y);
        data.set(key + "z", z);
        data.set(key + "yaw", yaw);
        data.set(key + "pitch", pitch);
    }

    /**
     * Deserialize a location from a {@link FileConfiguration}
     *
     * @param data The file configuration
     * @param key The key that the location has been serialized under
     * @return Location
     * @see #serializeLocation(Location, FileConfiguration, String)
     */
    public static Location deserializeLocation(FileConfiguration data, @Nonnull String key) {
        if (!key.isEmpty()) {
            key = key + ".";
        }

        String uid = data.getString(key + "world");
        double x = data.getDouble(key + "x");
        double y = data.getDouble(key + "y");
        double z = data.getDouble(key + "z");
        float yaw = Float.parseFloat(data.getString(key + "yaw"));
        float pitch = Float.parseFloat(data.getString(key + "pitch")); // For some reason FileConfiguration doesn't have getFloat :/

        return new Location(Bukkit.getWorld(UUID.fromString(uid)), x, y, z, yaw, pitch);
    }

    /**
     * Serializes a location to a single string
     *
     * @param location The location to serialize
     * @return Serialized Location. Format: world;x;y;z;yaw;pitch
     * @see #deserializeLocation(String)
     */
    public static String serializeLocation(Location location) {
        String uid = location.getWorld().getUID().toString();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        return uid +
                ";" +
                x +
                ";" +
                y +
                ";" +
                z +
                ";" +
                yaw +
                ";" +
                pitch;
    }

    /**
     * Deserializes a location
     * that has been serailized with {@link #serializeLocation(Location)}
     *
     * @param serialized Location as a serialized string
     * @return Location
     * @see #serializeLocation(Location)
     */
    public static Location deserializeLocation(String serialized) {
        String[] args = serialized.split(";");
        String uid = args[0];
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = Float.parseFloat(args[4]);
        float pitch = Float.parseFloat(args[5]);

        return new Location(Bukkit.getWorld(UUID.fromString(uid)), x, y, z, yaw, pitch);
    }

}

package tsp.mcnexus.server;

import org.bukkit.Bukkit;
import tsp.mcnexus.util.StringUtils;

import java.util.Optional;

/**
 * Utility class for a Minecraft Server
 *
 * @author TheSilentPro
 */
public final class ServerUtils {

    private ServerUtils() {}

    /**
     * Broadcast a message to the server. Colorized
     *
     * @param message The message to broadcast
     */
    public static void broadcast(String message) {
        Bukkit.broadcastMessage(StringUtils.colorize(message));
    }

    /**
     * Retrieve the server version
     *
     * @return The servers version
     * @see ServerVersion
     */
    public static Optional<ServerVersion> getVersion() {
        return ServerVersion.getVersion();
    }

    /**
     * Check if the server is running Paper
     *
     * @return Whether the server is running paper
     */
    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}

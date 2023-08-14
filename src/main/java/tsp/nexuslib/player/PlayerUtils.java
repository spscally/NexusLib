package tsp.nexuslib.player;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tsp.nexuslib.server.ServerVersion;
import tsp.nexuslib.util.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Utility class for a {@link Player}
 */
public final class PlayerUtils {

    private PlayerUtils() {}

    /**
     * Send a message to a {@link CommandSender} or {@link Player}
     *
     * @param receiver The receiver of the message
     * @param message The message to send
     * @param function Optional: Function to apply to the message
     * @param args Optional: Arguments for replacing. Format: {$argX} where X can be any argument number starting from 0.
     */
    public static void sendMessage(CommandSender receiver, String message, @Nullable UnaryOperator<String> function, @Nullable String... args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{$arg" + i + "}", args[i]);
            }
        }

        receiver.sendMessage(StringUtils.colorize(function != null ? function.apply(message) : message));
    }

    public static void sendMessage(CommandSender receiver, String message, @Nullable UnaryOperator<String> function) {
        sendMessage(receiver, message, function, (String[]) null);
    }

    public static void sendMessage(CommandSender receiver, String message, @Nullable String... args) {
        sendMessage(receiver, message, null, args);
    }

    public static void sendMessage(CommandSender receiver, String message) {
        sendMessage(receiver, message, null, (String[]) null);
    }

    public static void sendMessages(String message, CommandSender... receivers) {
        for (CommandSender receiver : receivers) {
            sendMessage(receiver, message);
        }
    }

    /**
     * Retrieve the ping of the {@link Player}
     * For display purposes only!
     *
     * @param player The player to check
     * @return The players ping. Returns -1 if an error occurs
     * @see Player#getPing()
     */
    public static int getPing(Player player) {
        Optional<ServerVersion> version = ServerVersion.getVersion();
        if (version.isPresent() && version.get().isNewerThanOrEquals(ServerVersion.v_1_17)) {
            return player.getPing();
        } else {
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                //e.printStackTrace();
                return -1;
            }
        }
    }

}

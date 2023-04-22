package tsp.nexuslib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import tsp.nexuslib.annotation.Internal;
import tsp.nexuslib.util.StringUtils;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a modified version of a command.
 *
 * @author TheSilentPro (Silent)
 */
public class NexusCommand implements CommandExecutor, TabCompleter {

    private final String name;
    private final String permission;
    private final String noPermissionMessage;

    private final CommandHandler commandHandler;
    private final TabHandler tabHandler;

    /**
     * Initialize a new {@link NexusCommand}.
     * You should use the {@link Builder} to create the sub-command!
     *
     * @param name                The name.
     * @param permission          The permission required. Set to null or empty for no permission.
     * @param noPermissionMessage The message sent if the sender doesn't have permission. Set to null or empty for no message.
     * @param commandHandler      The {@link CommandHandler} that will handle the execution. Set to null for no execution.
     * @param tabHandler          The {@link TabHandler} that will handle tab completions. Set to null for no tab completions.
     * @see Builder
     */
    public NexusCommand(@Nonnull String name, @Nullable String permission, @Nullable String noPermissionMessage, @Nullable CommandHandler commandHandler, @Nullable TabHandler tabHandler) {
        Validate.notNull(name, "Name can not be null!");

        this.name = name;
        this.permission = permission;
        this.noPermissionMessage = noPermissionMessage;
        this.commandHandler = commandHandler;
        this.tabHandler = tabHandler;
    }

    /**
     * @internal Used for internal purposes.
     * @see #commandHandler
     */
    @Internal
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        // Handle main command
        if (getPermission().isPresent() && (!getPermission().get().isEmpty() && !sender.hasPermission(getPermission().get()))) {
            getNoPermissionMessage().ifPresent(msg -> sender.sendMessage(StringUtils.colorize(msg)));
            return true;
        }

        getCommandHandler().ifPresent(handler -> handler.handle(sender, args));
        return true;
    }

    /**
     * @internal Used for internal purposes.
     * @see #tabHandler
     */
    @Internal
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (getPermission().isPresent() && (!getPermission().get().isEmpty() && !sender.hasPermission(getPermission().get()))) {
            return Collections.emptyList();
        }

        Optional<TabHandler> handler = getTabHandler();
        if (handler.isPresent()) {
            return handler.get().getCompletions(sender, args);
        } else {
            return Collections.emptyList();
        }
    }

    public void register(JavaPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(name);
        if (pluginCommand == null) {
            throw new NullPointerException("Command is not registered in plugin.yml: " + name);
        }

        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    public Optional<String> getNoPermissionMessage() {
        return Optional.ofNullable(noPermissionMessage);
    }

    public Optional<CommandHandler> getCommandHandler() {
        return Optional.ofNullable(commandHandler);
    }

    public Optional<TabHandler> getTabHandler() {
        return Optional.ofNullable(tabHandler);
    }

    public static class Builder {

        private String n;
        private String p;
        private String pm;
        private String isc;
        private Map<String, NexusCommand> sc;
        private CommandHandler h;
        private TabHandler th;

        public Builder() {}

        public Builder(String name) {
            this.n = name;
        }

        public Builder name(String name) {
            this.n = name;
            return this;
        }

        public Builder permission(@Nullable String permission) {
            this.p = permission;
            return this;
        }

        public Builder noPermissionMessage(@Nullable String message) {
            this.pm = message;
            return this;
        }

        public Builder handler(@Nullable CommandHandler handler) {
            this.h = handler;
            return this;
        }

        public Builder tabHandler(@Nullable TabHandler handler) {
            this.th = handler;
            return this;
        }

        public NexusCommand build() {
            return new NexusCommand(n, p, pm, h, th);
        }

    }

}

package tsp.mcnexus.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class NexusSubCommand extends NexusCommand {

    private final String[] aliases;

    /**
     * <strong>This is the same as {@link NexusCommand}, except with the option to add {@link String[] aliases}.</strong>
     *
     * Initialize a new {@link NexusSubCommand}.
     * You should use the {@link Builder} to create the command!
     *
     * @param name                The name.
     * @param permission          The permission required. Set to null or empty for no permission.
     * @param noPermissionMessage The message sent if the sender doesn't have permission. Set to null or empty for no message.
     * @param invalidSubCommandMessage The message sent if the sub-command does not exist. Set to null or empty for no message.
     * @param subCommands         The {@link NexusSubCommand sub-commands} for this command. Set to null for no sub-commands.
     * @param commandHandler      The {@link CommandHandler} that will handle the execution. Set to null for no execution.
     * @param tabHandler          The {@link TabHandler} that will handle tab completions. Set to null for no tab completions.
     * @param aliases             The
     * @see Builder
     */
    public NexusSubCommand(@Nonnull String name, @Nullable String permission, @Nullable String noPermissionMessage, @Nullable String invalidSubCommandMessage, @Nullable Map<String, NexusSubCommand> subCommands, @Nullable CommandHandler commandHandler, @Nullable TabHandler tabHandler, @Nullable String... aliases) {
        super(name, permission, noPermissionMessage, invalidSubCommandMessage, subCommands, commandHandler, tabHandler);
        this.aliases = aliases;
    }

    public Optional<String[]> getAliases() {
        return Optional.ofNullable(aliases);
    }


    public static class Builder {

        private String n;
        private String p;
        private String pm;
        private String isc;
        private Map<String, NexusSubCommand> sc;
        private CommandHandler h;
        private TabHandler th;
        private String[] a;

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

        public Builder invalidSubCommandMessage(@Nullable String message) {
            this.isc = message;
            return this;
        }

        public Builder subCommands(NexusSubCommand... subCommands) {
            for (NexusSubCommand sub : subCommands) {
                this.sc.put(sub.getName(), sub);
            }
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
        
        public Builder aliases(@Nullable String... aliases) {
            this.a = aliases;
            return this;
        }

        public NexusSubCommand build() {
            return new NexusSubCommand(n, p, pm, isc, sc, h, th, a);
        }

    }

}

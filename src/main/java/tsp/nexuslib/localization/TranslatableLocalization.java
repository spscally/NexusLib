package tsp.nexuslib.localization;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translatable Localization Utility Class.
 * <a href="https://gist.github.com/TheSilentPro/ce7d9ee24a8136f7aa486738a4b85e46">Gist (Source)</a>
 *
 * @author TheSilentPro (Silent)
 */
@ParametersAreNonnullByDefault
public class TranslatableLocalization {

    private final JavaPlugin plugin;
    private final String messagesPath;
    private final File container;
    private final String defaultLanguage;
    private final Map<UUID, String> languages; // Receiver UUID, Lang
    private final Map<String, FileConfiguration> data; // Lang, Data
    private String consoleLanguage;
    private boolean colorize;


    @SuppressWarnings("RegExpRedundantEscape")
    private final Pattern ARGS_PATTERN = Pattern.compile("(\\{\\$arg(\\d+)\\})", Pattern.CASE_INSENSITIVE); // (\{\$arg(\d+)\})

    /**
     * Creates a new {@link TranslatableLocalization} instance.
     *
     * @param plugin The plugin associated with this instance.
     * @param messagesPath The path for the messages in the /resources directory.
     * @param container The container for the messages. Default: {pluginDataFolder}/messages
     * @param defaultLanguage The default language file in your /resources directory. Default: en
     */
    public TranslatableLocalization(JavaPlugin plugin, String messagesPath, @Nullable File container, @Nullable String defaultLanguage) {
        notNull(plugin, "Plugin can not be null!");
        notNull(messagesPath, "Messages path can not be null!");

        this.plugin = plugin;
        this.messagesPath = messagesPath;
        this.container = container;
        this.defaultLanguage = defaultLanguage;
        this.languages = new HashMap<>();
        this.data = new HashMap<>();
        this.consoleLanguage = defaultLanguage;
        this.colorize = true;
    }

    /**
     * Create a new {@link TranslatableLocalization} instance with default parameters.
     * <bold>Notice: If you use this constructor make sure to FIRST create your {@link JavaPlugin#getDataFolder() plugins data folder} before calling the constructor!</bold>
     *
     * @param plugin The plugin associated with this instance.
     * @param messagesPath The path for the messages in the /resources directory.
     */
    public TranslatableLocalization(JavaPlugin plugin, String messagesPath) {
        this(plugin, messagesPath, new File(plugin.getDataFolder() + "/messages"), "en");
    }

    // Non-Console

    /**
     * Send message to the receiver.
     *
     * @param uuid The receiver.
     * @param message The message.
     * @see #sendMessage(UUID, String)
     */
    private void sendTranslatedMessage(UUID uuid, String message) {
        notNull(uuid, "UUID can not be null!");
        notNull(message, "Message can not be null!");
        if (message.isEmpty()) {
            return;
        }

        Entity receiver = Bukkit.getEntity(uuid);
        if (receiver == null) {
            //noinspection UnnecessaryToStringCall
            throw new IllegalArgumentException("Invalid receiver with uuid: " + uuid.toString());
        }

        receiver.sendMessage(message);
    }

    /**
     * Send a message to a receiver.
     *
     * @param receiver The receiver of the message.
     * @param key The key of the message.
     * @param function Optional: Function to apply to the message.
     * @param args Optional: Arguments for replacing. Format: {$argX} where X can be any argument number starting from 0.
     */
    public void sendMessage(UUID receiver, String key, @Nullable UnaryOperator<String> function, @Nullable String... args) {
        notNull(receiver, "Receiver can not be null!");
        notNull(key, "Key can not be null!");

        getMessage(receiver, key).ifPresent(message -> {
            if (args != null) {
                for (String arg : args) {
                    if (arg != null) {
                        Matcher matcher = ARGS_PATTERN.matcher(message);
                        while (matcher.find()) {
                            message = matcher.replaceAll(args[Integer.parseInt(matcher.group(2))]);
                        }
                    }
                }
            }

            // Apply function
            message = function != null ? function.apply(message) : message;
            sendTranslatedMessage(receiver, colorize ? ChatColor.translateAlternateColorCodes('&', message) : message);
        });
    }

    public void sendMessage(UUID receiver, String key, @Nullable UnaryOperator<String> function) {
        sendMessage(receiver, key, function, (String[]) null);
    }

    public void sendMessage(UUID receiver, String key, @Nullable String... args) {
        sendMessage(receiver, key, null, args);
    }

    public void sendMessage(UUID receiver, String key) {
        sendMessage(receiver, key, null, (String[]) null);
    }

    public void sendMessages(String key, UUID... receivers) {
        for (UUID receiver : receivers) {
            sendMessage(receiver, key);
        }
    }

    /**
     * Retrieve a message by the receiver's language and the key.
     *
     * @param uuid The receiver.
     * @param key The message key.
     * @return If present, the message, otherwise an empty {@link Optional}
     */
    @Nonnull
    public Optional<String> getMessage(UUID uuid, String key) {
        notNull(uuid, "UUID can not be null!");
        notNull(key, "Key can not be null!");

        FileConfiguration messages = data.get(languages.getOrDefault(uuid, defaultLanguage));
        if (messages == null) {
            return Optional.empty();
        }

        String message = messages.getString(key);
        if (message == null) {
            // Message not specified in language file, attempt to find it in the main one.
            messages = data.get(defaultLanguage);
            message = messages.getString(key);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && message != null) {
            message = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), message);
        }

        return Optional.ofNullable(message);
    }

    // Console

    private ConsoleMessageFunction logFunction = message -> Bukkit.getConsoleSender().sendMessage(message);

    public void sendTranslatedConsoleMessage(String message) {
        notNull(message, "Message can not be null!");
        if (message.isEmpty()) {
            return;
        }

        logFunction.logMessage(message);
    }

    public void sendConsoleMessage(String key, @Nullable UnaryOperator<String> function, @Nullable String... args) {
        notNull(key, "Key can not be null!");

        getConsoleMessage(key).ifPresent(message -> {
            if (args != null) {
                for (String arg : args) {
                    if (arg != null) {
                        Matcher matcher = ARGS_PATTERN.matcher(message);
                        while (matcher.find()) {
                            message = matcher.replaceAll(args[Integer.parseInt(matcher.group(2))]);
                        }
                    }
                }
            }

            // Apply function
            message = function != null ? function.apply(message) : message;
            sendTranslatedConsoleMessage(colorize ? ChatColor.translateAlternateColorCodes('&', message) : message);
        });
    }

    public void sendConsoleMessage(String key, @Nullable UnaryOperator<String> function) {
        sendConsoleMessage(key, function, (String[]) null);
    }

    public void sendConsoleMessage(String key, @Nullable String... args) {
        sendConsoleMessage(key, null, args);
    }

    public void sendConsoleMessage(String key) {
        sendConsoleMessage(key, null, (String[]) null);
    }

    public Optional<String> getConsoleMessage(String key) {
        notNull(key, "Key can not be null!");

        FileConfiguration messages = data.get(consoleLanguage != null ? consoleLanguage : defaultLanguage);
        if (messages == null) {
            return Optional.empty();
        }

        String message = messages.getString(key);
        if (message == null) {
            // Message not specified in language file, attempt to find it in the main one.
            messages = data.get(defaultLanguage);
            message = messages.getString(key);
        }

        return Optional.ofNullable(message);
    }

    public interface ConsoleMessageFunction {

        void logMessage(String message);

    }

    public void setLogFunction(ConsoleMessageFunction logFunction) {
        this.logFunction = logFunction;
    }

    // Auto Resolve

    public void sendMessage(CommandSender receiver, String key, @Nullable UnaryOperator<String> function, @Nullable String... args) {
        if (receiver instanceof ConsoleCommandSender || receiver instanceof RemoteConsoleCommandSender) {
            sendConsoleMessage(key, function, args);
        } else if (receiver instanceof Player player) {
            sendMessage(player.getUniqueId(), key, function, args);
        }
    }

    public void sendMessage(CommandSender receiver, String key, @Nullable UnaryOperator<String> function) {
        sendMessage(receiver, key, function, (String[]) null);
    }

    public void sendMessage(CommandSender receiver, String key) {
        sendMessage(receiver, key, null);
    }

    public void sendMessage(String key, CommandSender... receivers) {
        for (CommandSender receiver : receivers) {
            sendMessage(receiver, key);
        }
    }

    public void sendMessage(Message message) {
        // Non-Console
        message.getReceivers().forEach(uuid -> sendMessage(uuid, message.getText(), message.getFunction(), message.getArgs()));

        // Console
        if (message.shouldSendToConsole()) {
            sendConsoleMessage(message.getText(), message.getFunction(), message.getArgs());
        }
    }

    // Loader

    /**
     * Load all language files.
     *
     * @return Number of files loaded.
     */
    public int load() {
        File[] files = container.listFiles();
        if (files == null) {
            throw new NullPointerException("No files in container!");
        }

        int count = 0;
        for (File file : files) {
            String name = file.getName();
            // If the file is not of YAML type, ignore it.
            if (name.endsWith(".yml") || name.endsWith(".yaml")) {
                data.put(name.substring(0, name.lastIndexOf(".")), YamlConfiguration.loadConfiguration(file));
                count++;
            }
        }

        return count;
    }

    /**
     * Create the default language files from your /resources folder.
     *
     * @throws URISyntaxException URI Syntax Error
     * @throws IOException Error
     */
    public void createDefaults() throws URISyntaxException, IOException {
        URL url = plugin.getClass().getClassLoader().getResource(messagesPath);
        if (url == null) {
            throw new NullPointerException("No resource with path: " + messagesPath);
        }

        if (!container.exists()) {
            //noinspection ResultOfMethodCallIgnored
            container.mkdir();
        }

        // This is required otherwise Files.walk will throw FileSystem Exception.
        String[] array = url.toURI().toString().split("!");
        FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap<>());

        Files.walk(Paths.get(url.toURI()))
                .forEach(path -> {
                    try {
                        File out = new File(container.getAbsolutePath() + "/" + path.getFileName());
                        // If file is not of YAML type or if it already exists, ignore it.
                        if ((out.getName().endsWith(".yml") || out.getName().endsWith(".yaml")) && !out.exists()) {
                            Files.copy(path, out.toPath());
                        }

                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        fs.close();
    }

    /**
     * Retrieve the {@link JavaPlugin plugin} associated with this {@link TranslatableLocalization}.
     *
     * @return The plugin.
     */
    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Retrieve the {@link File container} for the language files.
     *
     * @return The container.
     */
    @Nonnull
    public File getContainer() {
        return container;
    }

    /**
     * Retrieve the message file path.
     *
     * @return The message file path.
     */
    @Nonnull
    public String getMessagesPath() {
        return messagesPath;
    }

    /**
     * Retrieve a {@link Map} containing all language/message data.
     *
     * @return The language/message data. Format: Language, Messages
     */
    public Map<String, FileConfiguration> getData() {
        return data;
    }

    /**
     * Retrieve the default language.
     *
     * @return The default language.
     */
    @Nonnull
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Retrieve a {@link Map} of all loaded language settings for each receiver.
     *
     * @return The language map. Format: Receiver, Language
     */
    @Nonnull
    public Map<UUID, String> getLanguageMap() {
        return languages;
    }

    /**
     * Retrieve the language for a specific receiver.
     *
     * @param uuid The uuid of the receiver.
     * @return Optional language.
     */
    @Nonnull
    public Optional<String> getLanguage(UUID uuid) {
        return Optional.ofNullable(languages.get(uuid));
    }

    /**
     * Set the language for a receiver.
     * This can be a {@link Player}.
     *
     * @param uuid The uuid of the receiver.
     * @param lang The language. (the language file's name, EXCLUDING EXTENSION!)
     */
    public void setLanguage(UUID uuid, String lang) {
        notNull(uuid, "UUID can not be null!");
        notNull(lang, "Lang can not be null!");

        languages.put(uuid, lang);
    }

    /**
     * Retrieve whether messages are colorized before sending.
     *
     * @return Colorize option value
     */
    public boolean isColorize() {
        return colorize;
    }

    /**
     * Set whether messages should be colorized before sending.
     *
     * @param colorize Colorize option
     */
    public void setColorize(boolean colorize) {
        this.colorize = colorize;
    }

    /**
     * Retrieve the language for the console.
     *
     * @return The consoles' language. Default: Default Language
     */
    @Nonnull
    public String getConsoleLanguage() {
        return consoleLanguage;
    }

    /**
     * Set the language for the console.
     *
     * @param consoleLanguage The consoles' language.
     */
    public void setConsoleLanguage(String consoleLanguage) {
        notNull(consoleLanguage, "Language can not be null!");
        this.consoleLanguage = consoleLanguage;
    }

    /**
     * Just a not-null validator.
     *
     * @param object The object to validate.
     * @param message The message sent if validation fails.
     * @param <T> Type
     */
    private <T> void notNull(T object, String message) {
        //noinspection ConstantConditions
        if (object == null) throw new NullPointerException(message);
    }

}

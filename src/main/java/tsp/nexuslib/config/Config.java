package tsp.nexuslib.config;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Better config handling.
 * <p>
 * Usage:
 *   Config config = (Config) YamlConfiguration.loadConfiguration(file);
 * </p>
 *
 * @author TheSilentPro (Silent)
 */
@SuppressWarnings("unused")
public class Config extends YamlConfiguration {

    @Nullable
    public Optional<Object> getOptional(@NotNull String path) {
        return Optional.ofNullable(super.get(path));
    }

    @NotNull
    public <T> Optional<T> getOptionalObject(@NotNull String path, @NotNull Class<T> clazz) {
        return Optional.ofNullable(super.getObject(path, clazz));
    }

    public Optional<Boolean> getOptionalBoolean(@NotNull String path) {
        return Optional.of(super.getBoolean(path));
    }

    public Optional<Color> getOptionalColor(@NotNull String path) {
        return Optional.ofNullable(super.getColor(path));
    }

    public Optional<Double> getOptionalDouble(@NotNull String path) {
        if (get(path) instanceof Number number) {
            return Optional.of(number.doubleValue());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Float> getFloat(@NotNull String path) {
        if (get(path) instanceof Number number) {
            return Optional.of(number.floatValue());
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    public Optional<Integer> getOptionalInt(@NotNull String path) {
        if (get(path) instanceof Number number) {
            return Optional.of(number.intValue());
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    public Optional<Long> getOptionalLong(@NotNull String path) {
        if (get(path) instanceof Number number) {
            return Optional.of(number.longValue());
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    public Number getNumber(@NotNull String path) {
        if (get(path) instanceof Number number) {
            return number;
        } else {
            return null;
        }
    }

    @NotNull
    public Optional<Number> getOptionalNumber(@NotNull String path) {
        return Optional.ofNullable(getNumber(path));
    }

    @NotNull
    public Optional<String> getOptionalString(@NotNull String path) {
        return Optional.ofNullable(super.getString(path));
    }

    @NotNull
    public Optional<ItemStack> getOptionalItemStack(@NotNull String path) {
        return Optional.ofNullable(super.getItemStack(path));
    }

    @NotNull
    public Optional<Location> getOptionalLocation(@NotNull String path) {
        return Optional.ofNullable(super.getLocation(path));
    }

    @NotNull
    public Optional<OfflinePlayer> getOptionalOfflinePlayer(@NotNull String path) {
        return Optional.ofNullable(super.getOfflinePlayer(path));
    }

    @NotNull
    public Optional<Configuration> getOptionalDefaults() {
        return Optional.ofNullable(super.getDefaults());
    }

    @NotNull
    public Optional<Configuration> getOptionalRoot() {
        return Optional.ofNullable(super.getRoot());
    }

    @NotNull
    public Optional<ConfigurationSection> getOptionalConfigurationSection(@NotNull String path) {
        return Optional.ofNullable(super.getConfigurationSection(path));
    }

    // Convenience methods

    public void setLocation(String path, Location location) {
        set(path, location);
    }

    public void setItemStack(String path, ItemStack itemStack) {
        set(path, itemStack);
    }

}

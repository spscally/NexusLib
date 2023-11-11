package tsp.nexuslib.config;

import com.google.common.annotations.Beta;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Config expansion.
 *
 * @author TheSilentPro (Silent)
 */
@Beta
@SuppressWarnings("unused")
public class Config {

    private FileConfiguration data;
    
    public Config(FileConfiguration data) {
        this.data = data;
    }

    public Config(File file) {
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public void reload(File file) {
        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public void reload(FileConfiguration data) {
        this.data = data;
    }

    @Nullable
    public Optional<Object> getOptional(@Nonnull String path) {
        return Optional.ofNullable(data.get(path));
    }

    @Nonnull
    public <T> Optional<T> getOptionalObject(@Nonnull String path, @Nonnull Class<T> clazz) {
        return Optional.ofNullable(data.getObject(path, clazz));
    }

    public Optional<Boolean> getOptionalBoolean(@Nonnull String path) {
        return data.contains(path) ? Optional.of(data.getBoolean(path)) : Optional.empty();
    }

    public Optional<Color> getOptionalColor(@Nonnull String path) {
        return Optional.ofNullable(data.getColor(path));
    }

    public Optional<Double> getOptionalDouble(@Nonnull String path) {
        if (data.get(path) instanceof Double n) {
            return Optional.of(n);
        } else {
            return Optional.empty();
        }
    }

    public float getFloat(@Nonnull String path) {
        return getFloat(path, 0);
    }

    public float getFloat(@Nonnull String path, float def) {
        if (data.get(path) instanceof Float n) {
            return n;
        } else {
            return def;
        }
    }

    public Optional<Float> getOptionalFloat(@Nonnull String path) {
        if (data.get(path) instanceof Float n) {
            return Optional.of(n);
        } else {
            return Optional.empty();
        }
    }

    @Nonnull
    public Optional<Integer> getOptionalInt(@Nonnull String path) {
        if (data.get(path) instanceof Integer n) {
            return Optional.of(n);
        } else {
            return Optional.empty();
        }
    }

    @Nonnull
    public Optional<Long> getOptionalLong(@Nonnull String path) {
        if (data.get(path) instanceof Long n) {
            return Optional.of(n);
        } else {
            return Optional.empty();
        }
    }

    @Nullable
    public Number getNumber(@Nonnull String path) {
        if (data.get(path) instanceof Number number) {
            return number;
        } else {
            return null;
        }
    }

    @Nonnull
    public Optional<Number> getOptionalNumber(@Nonnull String path) {
        return Optional.ofNullable(getNumber(path));
    }

    @Nonnull
    public Optional<String> getOptionalString(@Nonnull String path) {
        return Optional.ofNullable(data.getString(path));
    }

    @Nonnull
    public <T> Optional<List<T>> getOptionalList(@Nonnull String path, Class<T> clazz) {
        List<?> list = data.getList(path);
        if (list != null && !list.isEmpty() && list.get(0).getClass().isInstance(clazz)) {
            //noinspection unchecked
            return Optional.of((List<T>) list);
        } else {
            return Optional.empty();
        }
    }

    @Nonnull
    public <T extends ConfigurationSerializable> Optional<ConfigurationSerializable> getOptionalSerializable(@Nonnull String path, @Nonnull Class<T> clazz) {
        return Optional.ofNullable(data.getSerializable(path, clazz));
    }

    @Nonnull
    public Optional<Vector> getOptionalVector(@Nonnull String path) {
        return Optional.ofNullable(data.getVector(path));
    }

    @Nonnull
    public Optional<ItemStack> getOptionalItemStack(@Nonnull String path) {
        return Optional.ofNullable(data.getItemStack(path));
    }

    @Nonnull
    public Optional<Location> getOptionalLocation(@Nonnull String path) {
        return Optional.ofNullable(data.getLocation(path));
    }

    @Nonnull
    public Optional<OfflinePlayer> getOptionalOfflinePlayer(@Nonnull String path) {
        return Optional.ofNullable(data.getOfflinePlayer(path));
    }

    @Nonnull
    public Optional<Configuration> getOptionalDefaults() {
        return Optional.ofNullable(data.getDefaults());
    }

    @Nonnull
    public Optional<Configuration> getOptionalRoot() {
        return Optional.ofNullable(data.getRoot());
    }

    @Nonnull
    public Optional<ConfigurationSection> getOptionalConfigurationSection(@Nonnull String path) {
        return Optional.ofNullable(data.getConfigurationSection(path));
    }

    // Convenience methods

    public void setLocation(String path, Location location) {
        data.set(path, location);
    }

    public void setItemStack(String path, ItemStack itemStack) {
        data.set(path, itemStack);
    }

}

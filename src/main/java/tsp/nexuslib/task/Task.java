package tsp.nexuslib.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bukkit task.
 *
 * @author TheSilentPro (Silent)
 */
public interface Task extends Runnable {

    default Duration getDelay() {
        return Duration.ofMillis(-1);
    }

    default Duration getRepeatInterval() {
        return Duration.ofMillis(-1);
    }

    default long getDelayTicks() {
        return getDelay().toSeconds() * 20;
    }

    default long getRepeatIntervalTicks() {
        return getRepeatInterval().toSeconds() * 20;
    }

    default boolean isAsync() {
        return false;
    }

    /**
     * Schedules this {@link Task} using {@link BukkitScheduler}
     *
     * @param plugin Instance of the plugin which is scheduling this task
     * @return BukkitTask result
     */
    @Nonnull
    default BukkitTask schedule(@Nonnull JavaPlugin plugin) {
        Validate.notNull(plugin, "Plugin must not be null!");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        long repeat = this.getRepeatIntervalTicks();
        long delay = this.getDelayTicks();

        if (isAsync()) {
            if (repeat > -1 && delay > -1) {
                return scheduler.runTaskTimerAsynchronously(plugin, this, delay, repeat);
            } else if (repeat > -1) {
                return scheduler.runTaskTimerAsynchronously(plugin, this, 0, repeat);
            } else if (delay > -1) {
                return scheduler.runTaskLaterAsynchronously(plugin, this, delay);
            } else {
                return scheduler.runTaskAsynchronously(plugin, this);
            }
        } else {
            if (repeat > -1 && delay > -1) {
                return scheduler.runTaskTimer(plugin, this, delay, repeat);
            } else if (repeat > -1) {
                return scheduler.runTaskTimer(plugin, this, 0, repeat);
            } else if (delay > -1) {
                return scheduler.runTaskLater(plugin, this, delay);
            } else {
                return scheduler.runTask(plugin, this);
            }
        }
    }

    /**
     * Schedules multiple {@link Task}'s using {@link BukkitScheduler}
     *
     * @param plugin Instance of the plugin which is scheduling this task
     * @param tasks The tasks to schedule
     * @return BukkitTask result
     */
    @Nonnull
    static List<BukkitTask> schedule(@Nonnull JavaPlugin plugin, @Nonnull Task... tasks) {
        Validate.notNull(plugin, "Plugin must not be null!");
        Validate.notNull(tasks, "Tasks must not be null!");

        List<BukkitTask> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task.schedule(plugin));
        }

        return result;
    }

}
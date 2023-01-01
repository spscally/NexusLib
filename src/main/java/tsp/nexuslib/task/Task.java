package tsp.nexuslib.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bukkit task
 *
 * @author TheSilentPro
 */
public interface Task extends Runnable {

    /**
     * Delay before this task is run
     * 
     * @return The delay. If this is below 0 there will be no delay
     */
    default long getDelay() {
        return -1;
    }

    /**
     * Interval at which this task will repeat
     *
     * @return The interval. If this is below 0 the task will not repeat
     */
    default long getRepeatInterval() {
        return -1;
    }

    /**
     * If true the task will run asynchronously
     *
     * @return Whether this task should run asynchronously
     */
    default boolean isAsync() {
        return false;
    }

    /**
     * Schedules this {@link Task} using {@link BukkitScheduler}
     *
     * @param plugin Instance of the plugin which is scheduling this task
     * @return BukkitTask result
     */
    default BukkitTask schedule(@Nonnull JavaPlugin plugin) {
        Validate.notNull(plugin, "Plugin must not be null!");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if (isAsync()) {
            // Async
            if (this.getRepeatInterval() > -1) {
                return scheduler.runTaskTimerAsynchronously(plugin, this, this.getDelay(), this.getRepeatInterval());
            }

            if (this.getDelay() > -1) {
                return scheduler.runTaskLaterAsynchronously(plugin, this, this.getDelay());
            }

            return scheduler.runTaskAsynchronously(plugin, this);
        } else {
            // Sync
            if (this.getRepeatInterval() > -1) {
                return scheduler.runTaskTimer(plugin, this, this.getDelay(), this.getRepeatInterval());
            }

            if (this.getDelay() > -1) {
                return scheduler.runTaskLater(plugin, this, this.getDelay());
            }

            return scheduler.runTask(plugin, this);
        }
    }

    /**
     * Schedules multiple {@link Task}'s using {@link BukkitScheduler}
     *
     * @param plugin Instance of the plugin which is scheduling this task
     * @param tasks The tasks to schedule
     * @return BukkitTask result
     */
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

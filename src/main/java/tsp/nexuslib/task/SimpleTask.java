package tsp.nexuslib.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import tsp.nexuslib.util.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A simple task class based on {@link Task}.
 *
 * @author TheSilentPro (Silent)
 */
public class SimpleTask {

    private Runnable task;
    private long delay;
    private long repeat;
    private boolean async;

    public SimpleTask(@Nullable Runnable task) {
        this.task = task;
    }

    public SimpleTask() {
        this(null);
    }

    public SimpleTask task(@Nullable Runnable task) {
        this.task = task;
        return this;
    }

    public SimpleTask delay(@Nonnull Duration delay) {
        Validate.notNull(delay, "Delay must not be null!");
        this.delay = delay.toSeconds() * 20;
        return this;
    }

    public SimpleTask delay(long ticks) {
        this.delay = ticks;
        return this;
    }

    public SimpleTask repeat(@Nonnull Duration repeat) {
        Validate.notNull(repeat, "Repeat must not be null!");
        this.repeat = repeat.toSeconds() * 20;
        return this;
    }

    public SimpleTask repeat(long ticks) {
        this.repeat = ticks;
        return this;
    }

    public SimpleTask async(boolean async) {
        this.async = async;
        return this;
    }

    @Nonnull
    public Optional<Runnable> getTask() {
        return Optional.ofNullable(task);
    }

    public long getDelay() {
        return delay;
    }

    public long getRepeat() {
        return repeat;
    }

    public boolean isAsync() {
        return async;
    }

    /**
     * Schedules this {@link Task} using {@link BukkitScheduler}
     *
     * @param plugin Instance of the plugin which is scheduling this task
     * @return BukkitTask result
     */
    @Nonnull
    public BukkitTask schedule(@Nonnull JavaPlugin plugin) {
        Validate.notNull(plugin, "Plugin must not be null!");
        Validate.notNull(task, "Task must not be null!");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        if (isAsync()) {
            if (repeat > -1 && delay > -1) {
                return scheduler.runTaskTimerAsynchronously(plugin, task, delay, repeat);
            } else if (repeat > -1) {
                return scheduler.runTaskTimerAsynchronously(plugin, task, 0, repeat);
            } else if (delay > -1) {
                return scheduler.runTaskLaterAsynchronously(plugin, task, delay);
            } else {
                return scheduler.runTaskAsynchronously(plugin, task);
            }
        } else {
            if (repeat > -1 && delay > -1) {
                return scheduler.runTaskTimer(plugin, task, delay, repeat);
            } else if (repeat > -1) {
                return scheduler.runTaskTimer(plugin, task, 0, repeat);
            } else if (delay > -1) {
                return scheduler.runTaskLater(plugin, task, delay);
            } else {
                return scheduler.runTask(plugin, task);
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
    public static List<BukkitTask> schedule(@Nonnull JavaPlugin plugin, @Nonnull Task... tasks) {
        Validate.notNull(plugin, "Plugin must not be null!");
        Validate.notNull(tasks, "Tasks must not be null!");

        List<BukkitTask> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(task.schedule(plugin));
        }

        return result;
    }

}

package tsp.nexuslib.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Utility class for numbers
 *
 * @author TheSilentPro
 */
public final class NumberUtils {

    private NumberUtils() {}

    /**
     * Chance to do something
     *
     * @param chance The chance
     * @param max Max number
     * @param pass Ran upon passing
     * @param fail Ran upon failing
     */
    public static void chance(int chance, int max, Consumer<Integer> pass, Consumer<Integer> fail) {
        int picked = ThreadLocalRandom.current().nextInt(max);
        if (chance <= picked) {
            pass.accept(picked);
        } else {
            fail.accept(picked);
        }
    }

    public static void chance(int chance, Consumer<Integer> pass, Consumer<Integer> fail) {
        chance(chance, 100, pass, fail);
    }

    /**
     * Converts seconds to Minecraft ticks
     *
     * @param seconds Seconds to convert
     * @return Seconds in ticks
     */
    public static int toTicks(int seconds) {
        return seconds * 20;
    }

    /**
     * Converts a number to Machine number
     *
     * @param i Number to convert
     * @return Number as machine number
     */
    public static int toMachineNumber(int i) {
        return i - 1;
    }

}

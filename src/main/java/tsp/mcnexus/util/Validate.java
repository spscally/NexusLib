package tsp.mcnexus.util;

/**
 * Validation checks.
 *
 * @author TheSilentPro (Silent)
 */
public final class Validate {

    private Validate() {}

    public static <T> void notNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    public static <T> void notNull(T object) {
        notNull(object, "Object can not be null!");
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "Boolean can not be false!");
    }

    public static <T> boolean canCast(Object object, Class<T> type) {
        try {
            type.cast(object);
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

}

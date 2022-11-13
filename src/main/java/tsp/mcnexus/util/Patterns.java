package tsp.mcnexus.util;

import java.util.regex.Pattern;

/**
 * Utility class for common {@link Pattern}'s
 *
 * @author TheSilentPro
 */
public final class Patterns {

    private Patterns() {}

    public static final Pattern SPACE = Pattern.compile(" ");
    public static final Pattern COLON = Pattern.compile(":");
    public static final Pattern SEMI_COLON = Pattern.compile(";");
    public static final Pattern DOT = Pattern.compile("\\.");
    public static final Pattern COMMA = Pattern.compile(",");
    public static final Pattern DASH = Pattern.compile("-");
    public static final Pattern UNDERSCORE = Pattern.compile("_");
    public static final Pattern EQUALS = Pattern.compile("=");

    public static final Pattern HEX_CODE = Pattern.compile("(#[A-Fa-f0-9]{6})");

}

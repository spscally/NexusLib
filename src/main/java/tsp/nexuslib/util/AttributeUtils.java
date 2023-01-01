package tsp.nexuslib.util;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Utility class for handling {@link Attribute}'s
 *
 * @author TheSilentPro
 */
public final class AttributeUtils {

    /**
     * Cached values of {@link Attribute}
     */
    public static final Attribute[] ATTRIBUTES = Attribute.values();

    private AttributeUtils() {}

    /**
     * Checks if the {@link Attributable} has the specified {@link AttributeModifier}
     *
     * @param attributable The attributable to check
     * @param attribute The attribute to check
     * @param modifier The modifier to check
     * @return Whether the attribute contains the specified modifier. Also returns false if the attribute is not applicable to the attributable
     */
    public static boolean hasModifier(@Nonnull Attributable attributable, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance == null) {
            return false;
        }

        return instance.getModifiers().contains(modifier);
    }

    /**
     * Add an {@link AttributeModifier} to a {@link Attributable}
     *
     * @param attributable The attributable to modify
     * @param attribute The attribute to modify
     * @param modifier The modifier
     * @return Returns false if that attribute is not applicable to the attributable
     */
    public static boolean addModifier(@Nonnull Attributable attributable, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance == null) {
            return false;
        }
        instance.addModifier(modifier);

        return true;
    }

    /**
     * Remove an {@link AttributeModifier} from a {@link Attributable}
     *
     * @param attributable The attributable to modify
     * @param attribute The attribute to modify
     * @param modifier The modifier
     * @return Returns false if that attribute is not applicable to the attributable
     */
    public static boolean removeModifier(@Nonnull Attributable attributable, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance == null) {
            return false;
        }
        instance.removeModifier(modifier);

        return true;
    }

    /**
     * Set an {@link Attribute} for a {@link Attributable}
     *
     * @param attributable The attributable to modify
     * @param attribute The attribute to modify
     * @param value The value
     * @return Returns false if that attribute is not applicable to the attributable
     */
    public static boolean setAttribute(@Nonnull Attributable attributable, Attribute attribute, double value) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if (instance == null) {
            return false;
        }

        instance.setBaseValue(value);
        return true;
    }

    /**
     * Set multiple {@link Attribute}'s for a {@link Attributable}
     *
     * @param attributable The attributable to modify
     * @param attributes A map containing each attribute and their respective value for modification
     * @return Returns false if AT LEAST ONE attribute is not applicable to the attributable
     */
    public static boolean setAttributes(@Nonnull Attributable attributable, Map<Attribute, Double> attributes) {
        boolean result = true;

        for (Map.Entry<Attribute, Double> entry : attributes.entrySet()) {
            boolean entryResult = setAttribute(attributable, entry.getKey(), entry.getValue());
            if (result && !entryResult) {
                result = false;
            }
        }

        return result;
    }

    /**
     * Checks if the {@link Attribute} is applicable to the specified {@link Attributable}
     *
     * @param attributable The attributable to check
     * @param attribute The attribute to check
     * @return Whether the attributable has the specified attribute
     */
    public static boolean isApplicable(@Nonnull Attributable attributable, Attribute attribute) {
        return attributable.getAttribute(attribute) != null;
    }

}
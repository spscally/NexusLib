package tsp.mcnexus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provided by Guava - Included in spigot
 *
 * @author TheSilentPro (Silent)
 * @see com.google.common.annotations.Beta
 */
@Deprecated
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE
})
/*public*/ @interface Beta {}
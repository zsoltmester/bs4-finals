package hu.bsmart.framework.communication.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks composite objects
 * used in network messages.
 *
 * Currently does nothing, but later it can be used
 * by annotation processors to generate the validate
 * method of the objects.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Composite {
}

package gov.ismonnet.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the field is an {@link EventListener}
 * and that it should be considered as such by {@link EventBus}es.
 *
 * @author Ferlo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Listener {}

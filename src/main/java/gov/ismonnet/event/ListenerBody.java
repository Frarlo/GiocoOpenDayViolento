package gov.ismonnet.event;

import gov.ismonnet.util.LambdaUtils;

import java.io.Serializable;

/**
 * Class in charge of handling events
 *
 * This extends {@link Serializable} and should only
 * be implemented using lambdas so that {@link LambdaUtils#getLambdaMethod(Serializable)}
 * can be used to resolve the event class type
 *
 * @param <T> event type that this body handles
 * @author Ferlo
 */
@FunctionalInterface
public interface ListenerBody<T> extends Serializable {

    /**
     * Method in charge of handling events.
     *
     * This method should only be implemented using lambda expressions
     * so that {@link LambdaUtils#getLambdaMethod(Serializable)}
     * can be used to resolve the event class type.
     *
     * @param event event to handle
     */
    void handle(T event);
}

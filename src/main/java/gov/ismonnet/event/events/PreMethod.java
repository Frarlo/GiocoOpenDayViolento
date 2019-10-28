package gov.ismonnet.event.events;

import gov.ismonnet.event.EventBus;

/**
 * Events implementing this interface are able to
 * execute a method right before the listeners are notified.
 *
 * @author Ferlo
 *
 * @see EventBus#post(Object)
 */
public interface PreMethod {

    /**
     * Method invoked before the listeners have been notified
     * of this event
     */
    void preMethodCall();
}

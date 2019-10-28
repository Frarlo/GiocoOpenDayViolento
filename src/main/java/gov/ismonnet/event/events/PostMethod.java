package gov.ismonnet.event.events;

import gov.ismonnet.event.EventBus;

/**
 * Events implementing this interface are able to
 * execute a method after all the listeners have been notified.
 *
 * @author Ferlo
 *
 * @see EventBus#post(Object)
 */
public interface PostMethod {

    /**
     * Method invoked after all the listeners have been notified
     * of this event
     */
    void postMethodCall();
}

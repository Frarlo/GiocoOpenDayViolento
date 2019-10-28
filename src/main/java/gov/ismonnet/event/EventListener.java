package gov.ismonnet.event;

/**
 * Classes implementing this interface are able to
 * listen for a specific kind of event
 *
 * @param <T> event type that this listener can handle
 * @author Ferlo
 */
public interface EventListener<T> extends Comparable<EventListener> {

    /**
     * Notifies the listener of the given event
     *
     * @param event event
     */
    void invoke(T event);

    /**
     * Returns the event class type which this
     * listener can handle
     *
     * @return event class type
     */
    Class<T> getEventClass();
}

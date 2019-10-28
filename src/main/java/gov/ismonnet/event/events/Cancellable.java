package gov.ismonnet.event.events;

/**
 * Events implementing this interface are able to
 * indicate whether the action that invoked them should be cancelled.
 *
 * Subsequent listeners will still receive the event.
 *
 * @author Ferlo
 */
public interface Cancellable {

    /**
     * Indicates if the action that invoked this event should be cancelled
     *
     * @return true if the event is cancelled
     */
    boolean isCancelled();

    /**
     * Set whether the action that invoked this event should be cancelled
     *
     * @param isCancelled true to cancel the event
     */
    void setCancelled(boolean isCancelled);
}

package gov.ismonnet.event.events;

import gov.ismonnet.event.EventBus;

/**
 * Events implementing this interface are able to
 * delete themselves, preventing subsequent listeners
 * to receive the event
 *
 * @author Ferlo
 *
 * @see EventBus#post(Object)
 */
public interface Deletable {

    /**
     * Indicates whether this event is deleted and
     * subsequent listeners shouldn't receive it
     *
     * @return true if the event is deleted
     */
    boolean isDeleted();

    /**
     * Set whether the event is deleted and
     * whether subsequent listeners should receive it
     *
     * @param isDeleted true to delete the event
     */
    void setDeleted(boolean isDeleted);
}

package gov.ismonnet.event.listeners;

import gov.ismonnet.event.EventListener;

/**
 * Listener which has a priority
 *
 * @param <T> event type that this listener can handle
 * @author Ferlo
 */
public interface PriorityListener<T> extends EventListener<T> {

    /**
     * Low priority
     */
    int LOW_PRIORITY = 100;
    /**
     * The normal priority assigned to a listener
     */
    int NORMAL_PRIORITY = 150;
    /**
     * High priority
     */
    int HIGH_PRIORITY = 200;

    /**
     * Returns the priority of this event.
     *
     * An higher integer means that the listener is going to execute
     * before listeners with a lower one.
     *
     * @return integer representing the priority of this event
     */
    int getPriority();

    /**
     * Compares this object priority with the given one
     *
     * @param listener the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         has a greater priority than, equal to, or lower than the given object.
     */
    default int comparePriority(PriorityListener listener) {
        return comparePriority(listener.getPriority());
    }

    /**
     * Compares this object priority with the given one
     *
     * @param otherPriority the priority to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         has a greater priority than, equal to, or lower than the given one.
     */
    default int comparePriority(int otherPriority) {
        return Integer.compare(otherPriority, getPriority());
    }
}

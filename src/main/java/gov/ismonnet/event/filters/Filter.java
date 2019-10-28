package gov.ismonnet.event.filters;

import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.listeners.FilterableListener;

import java.util.function.BiPredicate;

/**
 * Restricts events accepted by a {@link FilterableListener}
 * only to the ones that match the specified predicate
 *
 * @param <T> event type
 * @author Ferlo
 */
@FunctionalInterface
public interface Filter<T> extends BiPredicate<EventListener<T>, T> {

    /**
     * Indicates whether the listener should be notified of the event
     *
     * @param listener listener that should be notified
     * @param event event to check
     * @return true if the listener should be notified
     */
    boolean test(EventListener<T> listener, T event);
}

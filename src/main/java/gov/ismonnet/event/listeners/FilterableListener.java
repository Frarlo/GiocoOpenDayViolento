package gov.ismonnet.event.listeners;

import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.filters.Filter;

import java.util.function.Predicate;

/**
 * Listener which is able to filter which event
 * should invoke its body
 *
 * @param <T> event type that this listener can handle
 * @author Ferlo
 */
public interface FilterableListener<T> extends EventListener<T>, Predicate<T> {

    /**
     * Returns the filters applied to this listener
     *
     * @return filters
     */
    Filter<T>[] getFilters();

    /**
     * Returns whether the listener should be notified of
     * the given event
     *
     * @param event event to check
     * @return true if the listener should be notified
     */
    @Override
    @SuppressWarnings("unchecked")
    default boolean test(T event) {
        if (getFilters().length > 0)
            for (Filter filter : getFilters())
                if (!filter.test(this, event))
                    return false;
        return true;
    }

}

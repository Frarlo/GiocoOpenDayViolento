package gov.ismonnet.event.bus;

import gov.ismonnet.util.ConcurrentWeakHashMap;

/**
 * Concurrent event bus which holds weak references
 * to the registered listeners.
 *
 * @param <T> event base class
 * @author Ferlo
 */
public class WeakBus<T> extends BaseBus<T> {

    /**
     * Constructs a concurrent event bus holding weak references
     * to the registered listeners
     */
    public WeakBus() {
        super(ConcurrentWeakHashMap::new, ConcurrentWeakHashMap::new);
    }
}

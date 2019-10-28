package gov.ismonnet.event.bus;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Concurrent event bus which holds strong references
 * to the registered listeners.
 *
 * @param <T> event base class
 * @author Ferlo
 */
public class StrongBus<T> extends BaseBus<T> {

    /**
     * Constructs a concurrent event bus holding strong references
     * to the registered listeners.
     */
    public StrongBus() {
        super(ConcurrentHashMap::new, ConcurrentHashMap::new);
    }
}

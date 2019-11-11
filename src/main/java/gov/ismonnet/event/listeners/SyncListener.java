package gov.ismonnet.event.listeners;

import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.ListenerBody;
import gov.ismonnet.event.filters.Filter;
import gov.ismonnet.util.LambdaUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Basic implementation of a listener which supports priority and filtering
 *
 * The listeners are compared by priority and, if they have the same
 * level, by object creation time.
 *
 * @param <T> event type that this listener can handle
 * @author Ferlo
 */
public class SyncListener<T> implements PriorityListener<T>, FilterableListener<T> {

    // Constants

    /**
     * Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(SyncListener.class);

    // Attributes

    /**
     * Body of the listener which handles the events
     */
    private final ListenerBody<T> body;
    /**
     * Class type of the events which this listener is able to handle
     */
    private final Class<T> eventTarget;
    /**
     * Priority of this listener
     */
    private final int priority;
    /**
     * Filters used to test events before notifying the body
     */
    private final Filter<T>[] filters;

    /**
     * Constructs an event listener with normal priority
     *
     * @param body body of the listener which handles the events
     * @param filters filters used to test events before notifying the body
     */
    @SafeVarargs
    public SyncListener(final ListenerBody<T> body, Filter<T>... filters) {
        this(body, NORMAL_PRIORITY, filters);
    }

    /**
     * Constructs an event listener
     *
     * @param body body of the listener which handles the events
     * @param priority listener priority
     * @param filters filters used to test events before notifying the body
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public SyncListener(final ListenerBody<T> body, final int priority, Filter<T>... filters) {
        this.body = body;
        this.eventTarget = (Class<T>) LambdaUtils.getLambdaMethod(body).getParameterTypes()[0];

        Objects.requireNonNull(eventTarget,"Event type cannot be null");

        this.priority = priority;
        this.filters = filters;
    }

    @Override
    public void invoke(T event) {
        try {
            if(test(event))
                body.handle(event);

        } catch (Throwable ex) {
            LOGGER.error("Error while invoking {} listener {}", event.getClass(), getClass(), ex);
        }
    }

    @Override
    public Class<T> getEventClass() {
        return this.eventTarget;
    }

    @Override
    public Filter<T>[] getFilters() {
        return filters;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(EventListener listener) {
        int discriminant = listener instanceof PriorityListener
                ? comparePriority((PriorityListener) listener)
                : comparePriority(NORMAL_PRIORITY);

        return discriminant == 0 ? System.identityHashCode(listener) - System.identityHashCode(this) : discriminant;
    }
}

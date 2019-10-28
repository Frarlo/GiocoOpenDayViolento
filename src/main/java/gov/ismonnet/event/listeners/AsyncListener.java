package gov.ismonnet.event.listeners;

import gov.ismonnet.event.ListenerBody;
import gov.ismonnet.event.filters.Filter;
import gov.ismonnet.util.ThreadFactoryBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Listeners which asynchronously executes its body
 * when notified
 *
 * @param <T> event type that this listener can handle
 * @author Ferlo
 */
public class AsyncListener<T> extends BaseListener<T> {

    // Constants

    /**
     * Executor used by default if none is specified in the constructor
     */
    private static final Executor DEFAULT_EXECUTOR = ForkJoinPool.getCommonPoolParallelism() > 1 ?
            ForkJoinPool.commonPool() :
            Executors.newFixedThreadPool(32, new ThreadFactoryBuilder()
                    .setNameFormat(i -> "Pdc_AsyncListener-" + i)
                    .setDaemon(true)
                    .build());

    // Attributes

    /**
     * Executor to asynchronously executes the listener body
     */
    private final Executor executor;

    /**
     * Constructs an async listener using {@link #DEFAULT_EXECUTOR}
     *
     * @see BaseListener#BaseListener(ListenerBody, Filter[])
     */
    @SafeVarargs
    public AsyncListener(ListenerBody<T> body, Filter<T>... filters) {
        this(body, DEFAULT_EXECUTOR, filters);
    }

    /**
     * Constructs an async listener using {@link #DEFAULT_EXECUTOR}
     *
     * @see BaseListener#BaseListener(ListenerBody, int, Filter[])
     */
    @SafeVarargs
    public AsyncListener(ListenerBody<T> body, int priority, Filter<T>... filters) {
        this(body, DEFAULT_EXECUTOR, priority, filters);
    }

    /**
     * Constructs an async listener
     *
     * @param executor executor used to invoke the body
     * @see BaseListener#BaseListener(ListenerBody, Filter[])
     */
    @SafeVarargs
    public AsyncListener(ListenerBody<T> body,
                         Executor executor,
                         Filter<T>... filters) {

        super(body, filters);
        this.executor = executor == null ? DEFAULT_EXECUTOR : executor;
    }

    /**
     * Constructs an async listener
     *
     * @param executor executor used to invoke the body
     * @see BaseListener#BaseListener(ListenerBody, int, Filter[])
     */
    @SafeVarargs
    public AsyncListener(ListenerBody<T> body,
                         Executor executor,
                         int priority,
                         Filter<T>... filters) {

        super(body, priority, filters);
        this.executor = executor == null ? DEFAULT_EXECUTOR : executor;
    }

    @Override
    public void invoke(T event) {
        executor.execute(() -> super.invoke(event));
    }
}

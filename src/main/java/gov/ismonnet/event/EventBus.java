package gov.ismonnet.event;

import gov.ismonnet.event.events.Deletable;
import gov.ismonnet.event.events.PostMethod;
import gov.ismonnet.event.events.PreMethod;

/**
 * Class which handles registering listeners and
 * posting events.
 *
 * @param <T> event base class
 * @param <D> listener base class
 * @author Ferlo
 */
public interface EventBus<T, D extends EventListener<? extends T>> {

    /**
     * Registers the given listener
     *
     * @param listener listener to register
     */
    void register(D listener);

    /**
     * Register all the listeners declared by this object and
     * by his subclasses
     *
     * A valid listener implements {@code D} and is
     * annotated with {@link Listener}
     *
     * @param obj object to register
     */
    void registerObj(Object obj);

    /**
     * Register the listeners declared by this object and
     * by his subclasses that handle one of the specified events.
     *
     * A valid listener implements {@code D} and is
     * annotated with {@link Listener}
     *
     * If the {@code events} param is empty, the method behaves
     * as {@link #registerObj(Object)}
     *
     * @param obj object to register
     * @param events events type to register
     */
    void registerObj(Object obj, Class<? extends T>... events);

    /**
     * Unregisters the given listener
     *
     * @param listener listener to unregister
     */
    void unregister(D listener);

    /**
     * Unregister all the listeners declared by this objector and
     * by his subclasses
     *
     * A valid listener implements {@code D} and is
     * annotated with {@link Listener}
     *
     * @param obj object to unregister
     */
    void unregisterObj(Object obj);

    /**
     * Unregister the listeners declared by this object and
     * by his subclasses that handle one of the specified events.
     *
     * A valid listener implements {@code D} and is
     * annotated with {@link Listener}
     *
     * If the {@code events} param is empty, the method behaves
     * as {@link #registerObj(Object)}
     *
     * @param obj object to unregister
     * @param events events type to register
     */
    void unregisterObj(Object obj, Class<? extends T>... events);

    /**
     * Posts an event on the bus and notifies all
     * the registered listeners.
     *
     * The method should correctly handle events implementing:
     * - {@link PreMethod}: invoke {@link PreMethod#preMethodCall()} before notifying the listeners
     * - {@link PostMethod}: invoke {@link PostMethod#postMethodCall()} after having notified the listeners
     * - {@link Deletable}: returns from the method without invoking the remaining listeners when
     *                      {@link Deletable#isDeleted()} returns true
     *
     * @param event event to post on the bus
     * @return the given event
     *
     * @see EventBus#post(Object)
     */
    T post(T event);
}

package gov.ismonnet.event.bus;

import gov.ismonnet.event.EventBus;
import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.Listener;
import gov.ismonnet.event.events.Deletable;
import gov.ismonnet.event.events.PostMethod;
import gov.ismonnet.event.events.PreMethod;
import gov.ismonnet.util.SneakyThrow;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Basic implementation of an event bus
 * which supports priority
 *
 * @param <T> event base class
 * @author Ferlo
 */
public class BaseBus<T> implements EventBus<T, EventListener<? extends T>> {

    /**
     * Cache containing the listeners previously found inside the keys
     */
    private final Map<Object, Set<EventListener<? extends T>>> listenerCache;
    /**
     * Map containing event types and their registered listeners
     */
    private final Map<Class, SortedSet<EventListener<? extends T>>> eventToListenerMap;

    /**
     * Constructs a new event bus
     *
     * @param listenerCacheSupplier supplies the map to use as a listener cache
     * @param eventToListenerMapSupplier supplies the map to use to register listeners
     */
    public BaseBus(Supplier<Map<Object, Set<EventListener<? extends T>>>> listenerCacheSupplier,
                   Supplier<Map<Class, SortedSet<EventListener<? extends T>>>> eventToListenerMapSupplier) {

        this.listenerCache = listenerCacheSupplier.get();
        this.eventToListenerMap = eventToListenerMapSupplier.get();
    }

    @Override
    public void register(EventListener<? extends T> listener) {
        eventToListenerMap.computeIfAbsent(listener.getEventClass(),
                s -> new ConcurrentSkipListSet<>()).add(listener);
    }

    @Override
    public void registerObj(Object obj) {
        extractListeners(obj).forEach(this::register);
    }

    @Override
    @SafeVarargs
    public final void registerObj(Object obj, Class<? extends T>... events) {
        if(events.length == 0) {
            registerObj(obj);
            return;
        }

        extractListeners(obj).stream()
                .filter(listener -> Arrays.stream(events).anyMatch(event -> listener.getEventClass() == event))
                .forEach(this::register);
    }

    @Override
    public void unregister(EventListener<? extends T> listener) {
        final SortedSet<EventListener<? extends T>> listeners = eventToListenerMap.get(listener.getEventClass());
        if (listeners != null && !listeners.isEmpty())
            listeners.remove(listener);
    }

    @Override
    public void unregisterObj(Object obj) {
        extractListeners(obj).forEach(this::unregister);
    }

    @Override
    @SafeVarargs
    public final void unregisterObj(Object obj, Class<? extends T>... events) {
        if(events.length == 0) {
            unregisterObj(obj);
            return;
        }

        extractListeners(obj).stream()
                .filter(listener -> Arrays.stream(events).anyMatch(event -> listener.getEventClass() == event))
                .forEach(this::unregister);
    }

    @Override
    public T post(T event) {

        if (event instanceof PreMethod)
            ((PreMethod) event).preMethodCall();

        final boolean isDeletable = event instanceof Deletable;
        if (isDeletable && ((Deletable) event).isDeleted())
            return event;

        @SuppressWarnings("unchecked")  // Shouldn't be a problem, all the listener
                                        // in the map should have their key as the event type
        final SortedSet<EventListener<T>> invokableLookup = (SortedSet) eventToListenerMap.get(event.getClass());

        if (invokableLookup != null && !invokableLookup.isEmpty())
            for (EventListener<T> listener : invokableLookup) {

                listener.invoke(event);

                if (isDeletable && ((Deletable) event).isDeleted())
                    return event;
            }

        if (event instanceof PostMethod)
            ((PostMethod) event).postMethodCall();

        return event;

    }

    /**
     * Extracts all the event listeners declared by the given object
     * and by his subclasses
     *
     * @param obj object to extract listeners from
     * @return set of extracted listeners
     */
    private Set<EventListener<? extends T>> extractListeners(Object obj) {

        return listenerCache.computeIfAbsent(obj, o -> {
            final List<Field> fields = new ArrayList<>();

            Class objClass = o.getClass();
            while(objClass != null && !objClass.getName().equals("java/lang/Object")) {
                fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
                objClass = objClass.getSuperclass();
            }

            return fields.stream()
                    .filter(field -> field.isAnnotationPresent(Listener.class))
                    .filter(field -> EventListener.class.isAssignableFrom(field.getType()))
                    .map(field -> (EventListener<? extends T>) // Without this cast gradle does not compile :thonking:
                            AccessController.doPrivileged((PrivilegedAction<EventListener<? extends T>>) () -> {
                                boolean isAccessible = field.isAccessible();
                                if(!isAccessible)
                                    field.setAccessible(true);
                                //noinspection unchecked
                                return SneakyThrow.callUnchecked(() -> (EventListener<? extends T>) field.get(o));
                            }))
                    // Todo: another filter to check if the event argument matches the generics
//                    .filter(listener -> T.class.isAssignableFrom(listener.getEventClass()))
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        });
    }
}

package gov.ismonnet.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Hacked together yay, mainly copied from {@link WeakHashMap}
 *
 * @param <K> key
 * @param <V> value
 * @author Ferlo
 */
public class ConcurrentWeakHashMap<K, V>
        extends AbstractMap<K, V>
        implements ConcurrentMap<K, V> {

    interface Key<K> {

        K get();

        @Override
        boolean equals(Object o);

        @Override
        int hashCode();
    }

    private static class WeakKey<K> extends WeakReference<K> implements Key<K> {

        private final int hash;

        WeakKey(K k) {
            super(k);
            hash = k.hashCode();
        }

        WeakKey(K k, ReferenceQueue<K> q) {
            super(k, q);
            hash = k.hashCode();
        }

        @Override
        public K get() {
            return super.get();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key that = (Key) o;
            return Objects.equals(get(), that.get());
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    private static class StrongKey<K> implements Key<K> {

        private K obj;
        private int hash;

        protected void setObject(K obj) {
            this.obj = obj;
            this.hash = obj != null ? obj.hashCode() : 0;
        }

        @Override
        public K get() {
            return obj;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key that = (Key) o;
            return Objects.equals(get(), that.get());
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    private final ConcurrentHashMap<Key<K>, V> map;
    private final ReferenceQueue<K> queue = new ReferenceQueue<>();

    public ConcurrentWeakHashMap() {
        this.map = new ConcurrentHashMap<>();
    }

    public ConcurrentWeakHashMap(int initialCapacity) {
        this.map = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentWeakHashMap(Map<? extends K, ? extends V> m) {
        this.map = new ConcurrentHashMap<>();
        putAll(m);
    }

    public ConcurrentWeakHashMap(int initialCapacity, float loadFactor) {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentWeakHashMap(int initialCapacity,
                                 float loadFactor,
                                 int concurrencyLevel) {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    // Internal utilities

    private static final ThreadLocal<StrongKey<Object>> LOOKUP_KEY = ThreadLocal.withInitial(StrongKey::new);
    private static final StrongKey NULL_KEY = new StrongKey() {

        @Override
        protected void setObject(Object obj) {}

        @Override
        public Object get() {
            return null;
        }
    };

    private static <K> Key<K> makeKey(K obj, ReferenceQueue<K> queue) {
        return obj == null ? NULL_KEY : new WeakKey<>(obj, queue);
    }

    @SuppressWarnings("unchecked")
    private static <K> StrongKey<K> makeLookupKey(Object obj) {
        if(obj == null)
            return NULL_KEY;
        StrongKey<Object> k = LOOKUP_KEY.get();
        k.setObject(obj);
        return (StrongKey<K>) k;
    }

    private static void releaseStrongKey(StrongKey<?> obj) {
        obj.setObject(null);
    }

    private void expungeStaleEntries() {
        Key<K> wk;
        //noinspection unchecked
        while((wk = (Key<K>) queue.poll()) != null) {
            map.remove(wk);
        }
    }

    // Map methods

    @Override
    public int size() {
        if (map.isEmpty())
            return 0;
        expungeStaleEntries();
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public V get(Object key) {
        StrongKey<K> lookupKey = makeLookupKey(key);
        V result = map.get(lookupKey);
        releaseStrongKey(lookupKey);
        return result;
    }

    @Override
    public boolean containsKey(Object key) {
        StrongKey<K> lookupKey = makeLookupKey(key);
        boolean result = map.containsKey(lookupKey);
        releaseStrongKey(lookupKey);
        return result;
    }

    @Override
    public V put(K key, V value) {
        expungeStaleEntries();
        Key<K> weakKey = makeKey(key, queue);
        return map.put(weakKey, value);
    }

    @Override
    public V remove(Object key) {
        expungeStaleEntries();
        StrongKey<K> lookupKey = makeLookupKey(key);
        V result = map.remove(lookupKey);
        releaseStrongKey(lookupKey);
        return result;
    }

    @Override
    public void clear() {
        expungeStaleEntries();
        map.clear();
    }

    private static class Entry<K, V> implements Map.Entry<K, V> {

        private final Map.Entry<?, V> entry;
        private final K key;

        Entry(Map.Entry<?, V> entry, K key) {
            this.entry = entry;
            this.key = key;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return entry.getValue();
        }

        @Override
        public V setValue(V value) {
            return entry.setValue(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry e = (Map.Entry)o;
            return Objects.equals(key, e.getKey()) && Objects.equals(getValue(), e.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return getClass().getName() + "{" +
                    "key=" + key +
                    ", value=" + getValue() +
                    '}';
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        private final Set<Map.Entry<Key<K>, V>> entrySet = map.entrySet();

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Iterator<Map.Entry<K, V>>() {

                private final Iterator<Map.Entry<Key<K>, V>> iterator = entrySet.iterator();
                private Entry<K, V> next;

                @Override
                public boolean hasNext() {
                    while (iterator.hasNext()) {
                        Map.Entry<Key<K>, V> entry = iterator.next();
                        Key<K> key = entry.getKey();
                        K keyObj = null;

                        if (key != null && key != NULL_KEY && (keyObj = key.get()) == null) // Reference expired, skip it
                            continue;

                        next = new Entry<>(entry, keyObj);
                        return true;
                    }
                    return false;
                }

                @Override
                public Map.Entry<K, V> next() {
                    if (next == null && !hasNext())
                        throw new NoSuchElementException();
                    
                    Entry<K, V> e = next;
                    next = null;
                    return e;
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }

        @Override
        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        @Override
        public int size() {
            int j = 0;
            for (Iterator i = iterator(); i.hasNext(); i.next()) j++;
            return j;
        }

        @Override
        public boolean remove(Object o) {

            if (!(o instanceof Map.Entry))
                return false;

            expungeStaleEntries();

            Map.Entry<?,?> entry = (Map.Entry<?,?>) o;

            StrongKey<K> lookupKey = makeLookupKey(entry.getKey());
            V value = map.get(lookupKey);

            boolean toRemove = Objects.equals(value, entry.getValue());
            if(toRemove && value == null) // If the value is null, check if it's actually contained
                toRemove = map.containsKey(lookupKey);

            if(toRemove)
                map.remove(lookupKey);

            releaseStrongKey(lookupKey);

            return toRemove;
        }

        @Override
        public int hashCode() {
            int h = 0;
            for (Map.Entry<Key<K>, V> entry : entrySet) {
                if (entry.getKey() == null)
                    continue;
                return Objects.hashCode(entry.getKey()) ^ Objects.hashCode(entry.getValue());
            }
            return h;
        }
    }

    private transient Set<Map.Entry<K,V>> entrySet;

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    @Override
    public V putIfAbsent(K key, V value) {
        expungeStaleEntries();
        return map.putIfAbsent(makeKey(key, queue), value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        expungeStaleEntries();
        //noinspection unchecked
        return map.remove(makeKey((K) key, queue), value);
    }

    @Override
    public V replace(K key, V newValue) {
        expungeStaleEntries();
        return map.replace(makeKey(key, queue), newValue);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        expungeStaleEntries();
        return map.replace(makeKey(key, queue), oldValue, newValue);
    }
}

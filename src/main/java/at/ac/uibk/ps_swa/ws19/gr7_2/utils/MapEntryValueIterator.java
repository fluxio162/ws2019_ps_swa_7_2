package at.ac.uibk.ps_swa.ws19.gr7_2.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Iterator that iterates only over the <em>values</em> of an Map.Entry iterator.
 *
 * @see Map
 * @see Map.Entry
 * @see Iterator
 * @see MapEntryKeyIterator
 * @param <K> key type of the Map
 * @param <V> value type of the Map
 */
public class MapEntryValueIterator<K, V> implements Iterator<V> {

    /**
     * Base iterator which iterates over Map.Entry instances.
     */
    private Iterator<Map.Entry<K, V>> baseIterator;

    /**
     *
     * @param baseIterator Base iterator which iterates over Map.Entry instances
     */
    public MapEntryValueIterator(Iterator<Map.Entry<K, V>> baseIterator) {
        this.baseIterator = baseIterator;
    }

    /**
     *
     * @param baseMap Map over whose values should be iterated
     */
    public MapEntryValueIterator(Map<K, V> baseMap) {
        this.baseIterator = baseMap.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    @Override
    public V next() {
        return baseIterator.next().getValue();
    }
}

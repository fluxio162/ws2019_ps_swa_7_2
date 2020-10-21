package at.ac.uibk.ps_swa.ws19.gr7_2.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Iterator that iterates only over the <em>keys</em> of an Map.Entry iterator.
 *
 * @see Map
 * @see Map.Entry
 * @see Iterator
 * @see MapEntryValueIterator
 * @param <K> key type of the Map
 * @param <V> value type of the Map
 */
public class MapEntryKeyIterator<K, V> implements Iterator<K> {

    /**
     * Base iterator which iterates over Map.Entry instances.
     */
    private Iterator<Map.Entry<K, V>> baseIterator;

    /**
     *
     * @param baseIterator Base iterator which iterates over Map.Entry instances
     */
    public MapEntryKeyIterator(Iterator<Map.Entry<K, V>> baseIterator) {
        this.baseIterator = baseIterator;
    }

    /**
     *
     * @param baseMap Map over whose keys should be iterated
     */
    public MapEntryKeyIterator(Map<K, V> baseMap) {
        this.baseIterator = baseMap.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return baseIterator.hasNext();
    }

    @Override
    public K next() {
        return baseIterator.next().getKey();
    }
}

package tsp.mcnexus.util;

/**
 * Represents a pair of a Key/Value.
 *
 * @param <K> The key
 * @param <V> The value
 */
public record Pair<K, V>(K k, V v) {

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }

}

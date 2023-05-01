package tsp.nexuslib.util.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Manager<K, V> {

    private final HashMap<K, V> registry = new HashMap<>();

    public Optional<V> set(K k, V v) {
        return Optional.ofNullable(this.registry.put(k, v));
    }

    public void setAll(Map<K, V> ref) {
        this.registry.putAll(ref);
    }

    public Optional<V> get(K k) {
        return Optional.ofNullable(this.registry.get(k));
    }

    public Map<K, V> getRegistry() {
        return registry;
    }

}
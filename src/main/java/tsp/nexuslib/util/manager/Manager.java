package tsp.nexuslib.util.manager;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Manager<K, V> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3620648365207650777L;

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

    public Optional<V> remove(K k) {
        return Optional.ofNullable(this.registry.remove(k));
    }

    public HashMap<K, V> getRegistry() {
        return registry;
    }

}
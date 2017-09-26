package cz.agents.dimap.tools;

import cz.agents.dimap.tools.sas.Variable;

import java.util.Objects;

/**
 * Created by pah on 12.8.15.
 */
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.getKey(), key) && Objects.equals(p.getValue(), value);
    }


    /*@Override
    public int hashCode() {
        System.out.println(key == null);
        System.out.println(value == null);
        System.out.println(key.hashCode());
        System.out.println(value.hashCode());
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }*/


    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

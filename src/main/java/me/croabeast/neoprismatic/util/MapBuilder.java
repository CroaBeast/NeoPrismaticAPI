package me.croabeast.neoprismatic.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapBuilder<A, B> {

    private final Map<A, B> map = new HashMap<>();
    private final List<BuilderEntry<A, B>> entries = new ArrayList<>();

    public MapBuilder<A, B> put(A key, B value) {
        map.put(key, value);
        entries.add(new BuilderEntry<>(key, value));
        return this;
    }

    public List<A> keys() {
        return entries.stream().map(e -> e.key).collect(Collectors.toList());
    }

    public List<B> values() {
        return entries.stream().map(e -> e.value).collect(Collectors.toList());
    }

    public List<BuilderEntry<A, B>> entries(){
        return new ArrayList<>(entries);
    }

    public Map<A, B> map() {
        return new HashMap<>(map);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public String toString() {
        return map.toString();
    }

    /**
     * Represents a pair of key and value of a map builder.
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class BuilderEntry<A, B> {
        /**
         * The key corresponding of this entry.
         */
        private final A key;
        /**
         * The value corresponding of this entry.
         */
        private final B value;
    }

    /**
     * Checks if a map builder is null or empty.
     *
     * @param builder a builder, can be null
     * @return true if builder is null or empty
     *
     * @param <A> key class
     * @param <B> value class
     */
    public static <A, B> boolean isEmpty(MapBuilder<A, B> builder) {
        return builder == null || builder.isEmpty();
    }
}

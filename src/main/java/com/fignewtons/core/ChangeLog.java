package com.fignewtons.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *  Created by dgruszcz on 9/18/16.
 *
 *  Keeps track of any graph changes (adding or deleting vertices / edges)
 *  since the last call to a graph algorithm.
 *
 */
public class ChangeLog<T> {

    private Map<Pair<T, T>, Integer> log;

    public ChangeLog() {
        log = new HashMap<>();
    }

    private void update(Pair<T, T> entry, Integer value) {
        log.put(entry, value);
    }

    private void addEntryIfNotPresent(Pair<T, T> entry) {
        if (!log.containsKey(entry)) {
            update(entry, 0);
        }
    }

    public void addVertex(T vertex) {
        Pair<T, T> entry = Pair.of(vertex, null);
        addEntryIfNotPresent(entry);
        update(entry, log.get(entry) + 1);
    }

    public void deleteVertex(T vertex) {
        Pair<T, T> entry = Pair.of(vertex, null);
        addEntryIfNotPresent(entry);
        update(entry, log.get(entry) - 1);
    }

    public void addEdge(Pair<T, T> edge) {
        addEntryIfNotPresent(edge);
        update(edge, log.get(edge) + 1);
    }

    public void deleteEdge(Pair<T, T> edge) {
        addEntryIfNotPresent(edge);
        update(edge, log.get(edge) - 1);
    }

    public void flush() {
        log.clear();
    }

    public Boolean hasChanged() {
        return log.values().stream().allMatch(value -> value != 0);
    }

    public Integer netChange() {
        return log.values().stream().mapToInt(Integer::intValue).sum();
    }

}

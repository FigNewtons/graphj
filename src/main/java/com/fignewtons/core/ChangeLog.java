package com.fignewtons.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Created by dgruszcz on 9/18/16.
 *
 *  Keeps track of any graph changes (adding or deleting vertices / edges)
 *  since the last call to a graph algorithm.
 *
 */
public class ChangeLog<T> {

    private static final Integer LOG_SIZE = 1000;

    private ArrayList<Pair<Pair<T, T>, Integer>> log;
    private Integer logId = 0;


    public ChangeLog() {
        reset();
    }

    private void reset() {
        log = new ArrayList<>(LOG_SIZE);
    }

    private void append(Pair<T, T> entry, Integer value) {
        Integer index = logId % LOG_SIZE;

        if (logId >= LOG_SIZE && index == 0) reset();

        log.add(index, Pair.of(entry, value));
        logId++;
    }

    public void addVertex(T vertex) {
        Pair<T, T> entry = Pair.of(vertex, null);
        append(entry, 1);
    }

    public void deleteVertex(T vertex) {
        Pair<T, T> entry = Pair.of(vertex, null);
        append(entry, -1);
    }

    public void addEdge(Pair<T, T> edge) {
        append(edge, 1);
    }

    public void deleteEdge(Pair<T, T> edge) {
        append(edge, -1);
    }

    private Map<Pair<T, T>, Integer> getAggregateLog(Integer logId) {

        Integer index = logId % LOG_SIZE;

        Map<Pair<T, T>, Integer> aggregateLog = new HashMap<>();

        for (int i = index; i < log.size(); i++) {

            Pair<T, T> entry = log.get(i).getLeft();
            Integer currentValue = log.get(i).getRight();

            if (aggregateLog.containsKey(entry)) {

                Integer aggregateValue = aggregateLog.get(entry);

                /**
                 *   Agg value  | Current value |   Result
                 *   -----------+---------------+-----------
                 *      1       |       1       |    1
                 *      0       |       1       |    1
                 *      -1      |       1       |    0
                 *      1       |       -1      |    0
                 *      0       |       -1      |    -1
                 *      -1      |       -1      |    -1
                 */
                if (aggregateValue != currentValue) {
                    aggregateLog.put(entry, aggregateValue + currentValue);
                }

            } else {
                aggregateLog.put(entry, currentValue);
            }
        }
        return aggregateLog;
    }

    public Integer getLogId() {
        return logId;
    }

    public Integer getLogSize() {
        return log.size();
    }

    public Boolean hasChangedSince(Integer logId) {
        Integer nextResetId = ((logId / LOG_SIZE) + 1) * LOG_SIZE;
        if (this.logId > nextResetId) return true;

        return getAggregateLog(logId).values().stream().allMatch(value -> value != 0);
    }

    public Integer netChangeSince(Integer logId) {
        return getAggregateLog(logId).values().stream().mapToInt(Integer::intValue).sum();
    }

}

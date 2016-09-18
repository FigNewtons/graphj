package com.fignewtons.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dgruszcz on 9/16/16.
 */
public interface Graph<T> {

    default void addVertices(Collection<T> vertices) {
        for (T vertex : vertices) {
            addVertex(vertex);
        }
    }

    default void addEdges(List<Pair<T, T>> edges) {
        for (Pair<T, T> edge : edges) {
            addEdge(edge);
        }
    }

    default void addEdge(Pair<T, T> edge) {
        addEdge(edge.getLeft(), edge.getRight());
    }

    default void deleteVertices(Collection<T> vertices) {
        for (T vertex : vertices) {
            deleteVertex(vertex);
        }
    }

    default void deleteEdges(List<Pair<T, T>> edges) {
        for (Pair<T, T> edge : edges) {
            deleteEdge(edge);
        }
    }

    default void deleteEdge(Pair<T, T> edge) {
        deleteEdge(edge.getLeft(), edge.getRight());
    }

    // Isolated vertices technically count
    default Set<T> getSourceVertices() {
        return getVertices().stream().filter(v -> isSource(v)).collect(Collectors.toSet());
    }

    default Set<T> getSinkVertices() {
        return getVertices().stream().filter(v -> isSink(v)).collect(Collectors.toSet());
    }

    default Pair<Set<T>, List<Pair<T, T>>> getGraphAsPair() {
        return Pair.of(getVertices(), getEdges());
    }

    default Boolean isEdge(Pair<T, T> edge) {
        return isEdge(edge.getLeft(), edge.getRight());
    }

    default Boolean hasIncomingEdge(T targetVertex) {
        for (T vertex : getVertices()) {
            if (getNeighbors(vertex).contains(targetVertex)) {
                return true;
            }
        }
        return false;
    }

    default Boolean hasOutgoingEdge(T sourceVertex) {
        return !getNeighbors(sourceVertex).isEmpty();
    }

    default Boolean isSource(T vertex) {
        return !hasIncomingEdge(vertex);
    }

    default Boolean isSink(T vertex) {
        return !hasOutgoingEdge(vertex);
    }

    void addVertex(T vertex);

    void addEdge(T sourceVertex, T destVertex);

    void deleteVertex(T vertex);

    void deleteEdge(T sourceVertex, T destVertex);

    Integer numVertices();

    Integer numEdges();

    Boolean isVertex(T vertex);

    Boolean isEdge(T sourceVertex, T destVertex);

    Boolean isEmpty();

    Set<T> getNeighbors(T vertex);

    Set<T> getVertices();

    List<Pair<T, T>> getIncidentEdges(T vertex);

    List<Pair<T, T>> getEdges();

    Graph<T> clone();

    Boolean hasChanged();

    void save();
}

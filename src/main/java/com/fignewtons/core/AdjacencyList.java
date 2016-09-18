package com.fignewtons.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 *  Created by dgruszcz on 9/16/16.
 *
 *  All getter methods return copies of the graph data. This prevents the caller
 *  from overwriting anything outside of the API.
 *
 *  TODO: Add a map relating edges to objects of type U (from which weights and other goodies can be derived)
 *  TODO: Cache derived return values and poll for changes
 */
public class AdjacencyList<T> implements Graph<T> {

    private Map<T, Set<T>> graph = new HashMap<>();
    private ChangeLog<T> changeLog = new ChangeLog<>();


    public AdjacencyList() {}

    public AdjacencyList(List<Pair<T, T>> edges) {
        addEdges(edges);
    }

    public AdjacencyList(Collection<T> vertices) {
        addVertices(vertices);
    }

    @Override
    public void addVertex(T vertex) {
        if (!isVertex(vertex)) {
            graph.put(vertex, new HashSet<T>());
            changeLog.addVertex(vertex);
        }
    }

    @Override
    public void addEdge(T sourceVertex, T destVertex) {
        addVertex(sourceVertex);
        graph.get(sourceVertex).add(destVertex);
        changeLog.addEdge(Pair.of(sourceVertex, destVertex));
    }

    @Override
    public void deleteVertex(T vertex) {
        graph.remove(vertex);
        changeLog.deleteVertex(vertex);

        for (T otherVertex : graph.keySet()) {
            deleteEdge(otherVertex, vertex);
        }
    }

    @Override
    public void deleteEdge(T sourceVertex, T destVertex) {
        if (isVertex(sourceVertex)) {
            Set<T> neighbors = graph.get(sourceVertex);
            if (neighbors.contains(destVertex)) {
                neighbors.remove(destVertex);
                changeLog.deleteEdge(Pair.of(sourceVertex, destVertex));
            }
        }
    }

    @Override
    public Integer numVertices() {
        return graph.keySet().size();
    }

    @Override
    public Integer numEdges() {
        return graph.values().stream().mapToInt(Set<T>::size).sum();
    }

    @Override
    public Boolean isVertex(T vertex) {
        return graph.containsKey(vertex);
    }

    @Override
    public Boolean isEdge(T sourceVertex, T destVertex) {
        return isVertex(sourceVertex) && graph.get(sourceVertex).contains(destVertex);
    }

    @Override
    public Boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public Boolean hasIncomingEdge(T targetVertex) {
        for (T vertex : graph.keySet()) {
            if (graph.get(vertex).contains(targetVertex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean hasOutgoingEdge(T sourceVertex) {
        return isVertex(sourceVertex) ? !graph.get(sourceVertex).isEmpty() : false;
    }

    @Override
    public Set<T> getNeighbors(T vertex) {
        return isVertex(vertex) ? new HashSet<>(graph.get(vertex)) : new HashSet<>();
    }

    @Override
    public Set<T> getVertices() {
        return new HashSet<>(graph.keySet());
    }

    @Override
    public Set<T> getSourceVertices() {
        Set<T> sourceVertices = getVertices();
        for (T vertex : graph.keySet()) {
            sourceVertices.removeAll(graph.get(vertex));
        }

        return sourceVertices;
    }


    @Override
    public List<Pair<T, T>> getIncidentEdges(T vertex) {
        if (!isVertex(vertex)) return null;

        List<Pair<T, T>> incidentEdges = new ArrayList<>();
        for (T neighbor : graph.get(vertex)) {
            incidentEdges.add(Pair.of(vertex, neighbor));
        }
        return incidentEdges;
    }

    @Override
    public List<Pair<T, T>> getEdges() {
        List<Pair<T, T>> edges = new ArrayList<>();
        for (T vertex : graph.keySet()) {
            edges.addAll(getIncidentEdges(vertex));
        }
        return edges;
    }

    public Map<T, Set<T>> getAdjacencyList() {
        Map<T, Set<T>> clonedGraph = new HashMap<>();
        for (T vertex : graph.keySet()) {
            clonedGraph.put(vertex, getNeighbors(vertex));
        }
        return clonedGraph;
    }

    @Override
    public Graph<T> clone() {
        AdjacencyList<T> clonedGraph = new AdjacencyList<>();
        clonedGraph.addVertices(getVertices());
        clonedGraph.addEdges(getEdges());
        return clonedGraph;
    }

    @Override
    public Boolean hasChanged() {
        return changeLog.hasChanged();
    }

    @Override
    public void save() {
        changeLog.flush();
    }

}

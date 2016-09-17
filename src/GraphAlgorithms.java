import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by dgruszcz on 9/16/16.
 */
public class GraphAlgorithms {

    public static <T> List<T> topologicalSort(Graph<T> graph) {

        // if (!graph.isDAG()) throw Exception("Not a directed acyclic graph");

        Graph<T> clonedGraph = graph.clone();
        List<T> sortedVertices = new ArrayList<>();

        Queue<T> sourceVertices = new LinkedList<>(clonedGraph.getSourceVertices());
        while (!sourceVertices.isEmpty()) {

            T currentVertex = sourceVertices.remove();
            sortedVertices.add(currentVertex);

            for (T neighbor : clonedGraph.getNeighbors(currentVertex)) {
                clonedGraph.deleteEdge(currentVertex, neighbor);
                if (clonedGraph.isSource(neighbor)) {
                    sourceVertices.add(neighbor);
                }
            }
        }

        if (clonedGraph.getEdges().isEmpty()){
            return sortedVertices;
        } else {
            // TODO: Throw a 'has cycle' exception since
            return null;
        }
    }


    public static void main(String[] args) {

        List<Pair<Integer, Integer>> edges = Arrays.asList(
                Pair.of(3, 8),
                Pair.of(3, 10),
                Pair.of(5, 11),
                Pair.of(7, 8),
                Pair.of(7, 11),
                Pair.of(8, 9),
                Pair.of(11, 2),
                Pair.of(11, 9),
                Pair.of(11, 10)
        );

        Graph<Integer> graph = new AdjacencyList<>(edges);

        System.out.println(topologicalSort(graph));

    }
}

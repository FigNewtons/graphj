package com.fignewtons.core;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dgruszcz on 9/18/16.
 */
public class ChangeLogTest {

    public static ChangeLog<Integer> changeLog = new ChangeLog<>();
    public static Integer currentLogId;
    public static String message;

    @Before
    public void setCurrentLogId() {
        currentLogId = changeLog.getLogId();
    }


    @Test
    public void testInsertThenDeleteVertex() {
        message = "Insertion followed by deletion yields no net change";
        changeLog.addVertex(1);
        changeLog.deleteVertex(1);
        Assert.assertFalse(message, changeLog.hasChangedSince(currentLogId));
    }

    @Test
    public void testDeleteThenInsertVertex() {
        message = "Deletion followed by insertion yields no net change";
        changeLog.deleteVertex(1);
        changeLog.addVertex(1);
        Assert.assertFalse(message, changeLog.hasChangedSince(currentLogId));
    }

    @Test
    public void testInsertThenDeleteEdge() {
        message = "Insertion followed by deletion yields no net change";
        changeLog.addEdge(Pair.of(1, 2));
        changeLog.deleteEdge(Pair.of(1, 2));
        Assert.assertFalse(message, changeLog.hasChangedSince(currentLogId));
    }

    @Test
    public void testDeleteThenInsertEdge() {
        message = "Deletion followed by insertion yields no net change";
        changeLog.deleteEdge(Pair.of(1, 2));
        changeLog.addEdge(Pair.of(1, 2));
        Assert.assertFalse(message, changeLog.hasChangedSince(currentLogId));
    }

    @Test
    public void testDirectedEdges() {
        message = "Edges (1,2) and (2,1) count as separate entries";
        changeLog.addEdge(Pair.of(1, 2));
        changeLog.deleteEdge(Pair.of(2, 1));
        Assert.assertTrue("Net change should be zero", changeLog.netChangeSince(currentLogId) == 0);
        Assert.assertTrue(message, changeLog.hasChangedSince(currentLogId));
    }

    @Test
    public void testChangeLogOnGraph() {

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
        Assert.assertTrue("Change log should change", graph.hasChangedSince(currentLogId));

        currentLogId = graph.getChangeId();
        graph.deleteVertex(8);
        graph.addEdge(3, 8);
        graph.addEdge(7, 8);
        graph.addEdge(8, 9);
        Assert.assertFalse("Graph is back to original state", graph.hasChangedSince(currentLogId));
    }


}

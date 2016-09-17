import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by dgruszcz on 9/16/16.
 */
public interface GraphClassification<T> {


    enum Orientation {
        UNDIRECTED,
        DIRECTED,
        MIXED;
    }

    enum Weights {
        UNWEIGHTED,
        WEIGHTED;
    }

    Boolean isPlanar();

    Boolean isTree();

    Boolean isDAG();


    void containsCycle();


    Set<T> getConnectedComponents();

    void getStronglyConnectedComponents();


    void numConnectedComponents();

    void numStronglyConnectedComponents();


}

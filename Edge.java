/*
 * This is the file which creates an Edge class and is used for creating Edge objects of the graph.
 * It is called from the RunExperiments class.
 */

public class Edge {
    int v1, v2;
    public Edge(int v1, int v2) {
        if (v1 >= v2) throw new IllegalArgumentException("v1 should be smaller than v2!");
        this.v1 = v1;
        this.v2 = v2;
    }
    int otherNode(int value) {
        return (this.v1 == value ? this.v2 : this.v1);
    }
}
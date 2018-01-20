/*
 * This file holds the Graph class which helps create the Graph structure with Edge objects and vertices
 */

import java.util.*;

public class Graph {

    int edgeNum;
    List<Edge>[] nodes;
    ArrayList<Integer> vertices;
    ArrayList<Edge> edges;
    public Graph (int nodeNum, int edgeNum) {
        nodes = new List[nodeNum];
        vertices = new ArrayList<>();
        this.edgeNum = edgeNum;
        this.edges = new ArrayList<>();
    }
}

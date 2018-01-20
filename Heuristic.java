/*
* This file performs Construction Heuristics on the given datasets using the Maximum Degree Greedy algorithm.
* It takes in the data parsed in the form of a graph containing edge objects.
* It starts with generate a table for each nodes' degree.
* In each iteration, it picks a node with largest degrees in remaining nodes, delete this node and its edges and update the degrees.
**/

import java.util.*;
public class Heuristic implements Solver{

    Graph graph;
    List<Integer> res;
    List<String> trace;
    double cutoff;
    long startTime;

    public Heuristic(Graph g, double cutoff) {
        this.graph = g;
        this.cutoff = cutoff;
        trace = new ArrayList<>();
        startTime = System.currentTimeMillis();
        solve();
        long endTime = System.currentTimeMillis();
        double duration = (double)(endTime - startTime) / 1000;
        trace.add(String.format("%.2f", duration) + "," + res.size());
        System.out.println(trace);
    }

    private void solve() {

        List<Integer> vertexCover = new ArrayList<>();
        int edgeNum = graph.edgeNum;
        Map<Integer, Integer> nodeDegrees = new HashMap<>(graph.nodes.length);
        for (int i = 0; i < graph.nodes.length; i++) {
            nodeDegrees.put(i, (graph.nodes[i].size()));
        }
        while(edgeNum > 0) {
            int maxDegree = Integer.MIN_VALUE;
            int nodeToDelete = -1;
            for (Map.Entry<Integer, Integer> entry : nodeDegrees.entrySet()) {
                int degree = entry.getValue();
                if (degree > maxDegree) {
                    maxDegree = degree;
                    nodeToDelete = entry.getKey();
                }
            }
            System.out.println("nodeToDelete: " + nodeToDelete);
            int deletedEdgeNum = 0;
            for (Edge edge : graph.nodes[nodeToDelete]) {
                int neighbor = edge.otherNode(nodeToDelete);
                if (nodeDegrees.containsKey(neighbor)) {
                    nodeDegrees.put(neighbor, nodeDegrees.get(neighbor) - 1);
                    deletedEdgeNum++;
                }
            }
            System.out.println("deletedEdgeNum: " + deletedEdgeNum);
            nodeDegrees.remove(nodeToDelete);
            edgeNum -= deletedEdgeNum;
            vertexCover.add(nodeToDelete);
            System.out.println("edgeNum: " + edgeNum);
        }
        this.res = vertexCover;
    }

    @Override
    public List<Integer> getResult() {
        return res;
    }
    @Override
    public List<String> getTrace() {
        return trace;
    }
}

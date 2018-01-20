/*
* This file performs Branch-and-Bound algorithm on the given datasets.
* It takes in the data parsed in the form of a graph containing edge objects.
* It sets initial upper bound as Integer.MAX_VALUE.
* Backtrack in a typical recursion structure.
* The branch is cut off when its length is no shorter than best solution we've found, which means the lower bound is 1.
* */


import java.util.*;

public class Bnb implements Solver{
    Graph graph;
    int bestResult;
    boolean[] presence;
    List<Integer> intermRes;
    List<Integer> result;
    List<String> trace;
    long startTime;
    double cutoff;

    public Bnb(Graph g, double cutoff) {
        this.graph = g;
        this.cutoff = cutoff;
        startTime = System.currentTimeMillis();
        trace = new ArrayList<>();
        bestResult = Integer.MAX_VALUE; // MAX_VALUE as initial upper bound
        intermRes = new ArrayList<>();
        presence = new boolean[graph.nodes.length]; // record whether node is used



        solve(0);
    }

    private void solve(int start) {
        if (start == graph.nodes.length) { // have checked all nodes

            if (intermRes.size() < bestResult) { // if this result is better than upper bound, update it
                result = new ArrayList<>(intermRes);
                bestResult = intermRes.size();

                long endTime = System.currentTimeMillis();
                double duration = (double)(endTime - startTime) / 1000;
                trace.add(String.format("%.2f", duration) + "," + bestResult);
                System.out.println(trace.get(trace.size() - 1));
            }
            return;
        }
        for (int i = start; i < graph.nodes.length; i++) {
            if (intermRes.size() >= bestResult - 1) break; // if the size of current solution plus lower bound (which is 1) is greater than upper bound, cut off

            int k = 0;
            List<Edge> neighbors = graph.nodes[i];
            while(k < neighbors.size() && presence[neighbors.get(k).otherNode(i)]) { // count how many neighbor covered
                k++;
            }
            if (k == neighbors.size()) continue; // if all neighbor covered, don't need to add this one
            presence[i] = true; // add this node in solution
            intermRes.add(i);
            if ((System.currentTimeMillis() - startTime) / 1000 > cutoff) return;
            solve(i + 1); // go check remaining graph
            intermRes.remove(intermRes.size() - 1);//remove it
            presence[i] = false;
            while(k < neighbors.size() && (neighbors.get(k).otherNode(i) > i || presence[neighbors.get(k).otherNode(i)])) { // count how many neighbors that have smaller index are covered
                k++;
            }
            if (k < neighbors.size()) { // if any of the neighbors with smaller index are not covered, we can't skip this node, otherwise will leave some edge never covered. So break
                break;
            }
        }

    }

    @Override
    public List<Integer> getResult(){
        return result;
    }
    @Override
    public List<String > getTrace(){
        return trace;
    }

}

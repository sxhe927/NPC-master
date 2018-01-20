/*
* This file performs local search on the given datasets using the Simulated Annealing algorithm.
* It takes in the data parsed in the form of a graph containing edge objects, the cutoff time and a random seed.
* It starts with finding an initial Vertex Cover using a Greedy approach for maximum degrees of vertices.
* The cost of a solution is given by the size of the solution.
* An initial temperature and a cooling amount is specified & the algorithm keeps running until either the cutoff time is
* reached or the temperature goes below a given threshold. At every stage, the temperature is reduced. The logic of this
* algorithm is that when at a higher temperature, the algorithm tends to accept worse solutions and it gets better with cooling
* */

/*
* The logic of removing a vertex from a candidate VC is if all its neighbors exist in the VC, it can safely be removed
* because its edges still remain covered.
* The VC is updated if the cost is lesser than the previous VC.
* */

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
public class LS2 implements Solver{

    Graph graph;
    int seed;
    double cutoff;
    List<Integer> res;
    List<String> trace;
    String output_file_random_trace;

    public LS2(Graph g, double cutoff, int seed, String file) {
        this.graph = g;
        this.seed = seed;
        this.cutoff = cutoff;
        this.output_file_random_trace = file;
        try {
            solve();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    //@Override
    private void solve() throws Exception {
        PrintWriter outputRandom = new PrintWriter(output_file_random_trace, "UTF-8");
        DecimalFormat twoDForm = new DecimalFormat("#.##");

        long start = System.currentTimeMillis();
        double elapsedFinal = 0;

        // Temperature Start and Cooling
        double temp = 20000.0;
        double cooling = 0.9999999;
        double thresh = 0.00001;

        // initialize a Random Generator
        Random randomno = new Random();
        randomno.setSeed(seed);
        double diff;

        ArrayList<Integer> current = maxDegGreedy();                            //start with getting the initial VC using a
        int currCost = current.size();                                          //greedy approach for largest degrees
        ArrayList<Integer> nextSol;

        int[] params;                                              //checking if solution is a VC and the new cost of it
        // Use Simulated Annealing to find MVC and continue till temp > threshold or for the time
        while (elapsedFinal < cutoff) {
            params = new int[2];
            nextSol = getNextSol(current, randomno, params);                    //finding the new candidate solution
            if(params[1] != -1) {                                               //only if the new solution is a valid VC
                diff = params[1] - currCost;                                    //find difference between solutions
                //condition for using new solution
                if ((diff < 0) || (currCost > 0 && Math.exp(-Math.abs(diff) / temp) > randomno.nextDouble())) {
                    current = nextSol;
                    currCost = params[1];
                }
            }
            temp *= cooling;                                                    //reducing the temperature

            String elapsed = twoDForm.format((System.currentTimeMillis() - start) / 1000.0);
            if(params[0] == 1) {                                                //storing the new VC
                System.out.println();
                System.out.print("New VC size is: " + current.size());
                StringBuilder sb = new StringBuilder();
                sb.append(elapsed).append(", ").append(current.size());
                outputRandom.println(sb.toString());
            }
            elapsedFinal = System.currentTimeMillis() - start;
        }

        String elapsed = twoDForm.format((System.currentTimeMillis() - start) / 1000.0);
        outputRandom.close();
        System.out.print("Final VC size is: " + current.size());
        System.out.println("Time taken is: " + elapsed);
        this.res = current;                                                     //returning the final answer
    }

    public ArrayList<Integer> getNextSol(ArrayList<Integer> current, Random randomno, int [] params) {

        ArrayList<Integer> copyCurrent = new ArrayList<>(current);

        int index = randomno.nextInt(copyCurrent.size());
        int vertex = copyCurrent.get(index);                                    //picking a random vertex to remove from current VC
        int result = 1;

        //ensuring that the new list is still a VC
        List<Edge> e = graph.nodes[vertex];
        for (Edge ed : e) {
            int other = ed.otherNode(vertex);
            if (!copyCurrent.contains(other)) {
                result = 0;
                break;
            }
        }
        if (result == 1) {                                                      //only if the new solution is a VC, the cost is the
            copyCurrent.remove(new Integer(vertex));                            //degree of the vertex that is removed.
            params[1] = graph.nodes[vertex].size();
        } else {
            params[1] = -1;
        }
        params[0] = result;
        return copyCurrent;
    }

    private ArrayList<Integer> maxDegGreedy() {
        ArrayList<Integer> output = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>(graph.edges);

        ArrayList<Integer> sortedVertices = sortVertices();
        int i =0 ;
        int temp;

        while (!edges.isEmpty() && i < sortedVertices.size()) {
            temp = sortedVertices.get(i);
            output.add(temp);
            removeEdgesFromList(edges, temp);                                   //once vertex is added, remove corresponding edges
            i++;                                                                //from list as they have been covered.
        }
        return output;
    }

    private static void removeEdgesFromList(ArrayList<Edge> edges, int temp) {
        ArrayList<Edge> copy = new ArrayList<>(edges);
        for(Edge e: copy) {
            if(e.v1 == temp || e.v2 == temp)
                edges.remove(e);
        }
    }

    private ArrayList<Integer> sortVertices() {
        ArrayList<Integer> vertices = new ArrayList<>(graph.vertices);
        Collections.sort(vertices, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(graph.nodes[b].size(), graph.nodes[a].size());
            }
        });
        return vertices;
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

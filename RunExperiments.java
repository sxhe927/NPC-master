/*
CSE6140 project
*/
/*
 * This file is the main starter for the project.
 * It takes in the inputs, checks for the wrong inputs and throws exceptions.
 * It creates the PrintWriter objects to write both the solution and trace files.
 * It also parses the input and creates the Graph structure.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class RunExperiments {
	public static void main(String[] args) throws Exception {

		if (args.length < 4) {
			System.err.println("Unexpected number of command line arguments");
			System.exit(1);
		}

		String inst = "", alg = "", time = "", seed = "";
		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("-inst"))
				inst = args[i + 1];
			if (args[i].equals("-alg"))
				alg = args[i + 1];
			if (args[i].equals("-time"))
				time = args[i + 1];
			if (args[i].equals("-seed"))
				seed = args[i + 1];
		}
		if (alg.equals("") || inst.equals("") || time.equals("")) {
			System.out.println("Incorrect input");
			System.exit(0);
		}
		if ((alg.equals("LS1") || alg.equals("LS2")) && (seed.equals(""))) {
			System.out.println("Seed is required for local search to run");
			System.exit(0);
		}

		String output_sol = "", output_trace = "";
		int end = inst.indexOf(".graph");
		int start = inst.lastIndexOf("/");
		if (alg.equals("BnB") || alg.equals("Approx")) {
			output_sol = inst.substring(start + 1, end) + "_" + alg + "_" + time + ".sol";
			output_trace = inst.substring(start + 1, end) + "_" + alg + "_" + time + ".trace";
		} else {
			output_sol = inst.substring(start + 1, end) + "_" + alg + "_" + time + "_" + seed + ".sol";
			output_trace = inst.substring(start + 1, end) + "_" + alg + "_" + time + "_" + seed + ".trace";
		}

		PrintWriter sol_writer = new PrintWriter(output_sol, "UTF-8");
		PrintWriter trace_writer = new PrintWriter(output_trace, "UTF-8");
		Graph G = parseGraph(inst);
		Solver solver = null;

		if (alg.equals("Approx")) {
			solver = new Heuristic(G, Double.parseDouble(time));
		} else if (alg.equals("BnB")) {
			solver = new Bnb(G, Double.parseDouble(time));
		} else if (alg.equals("LS1")) {
			solver = new LS1(G, Double.parseDouble(time) * 1000, Integer.parseInt(seed));
		} else if (alg.equals("LS2")) {
			solver = new LS2(G, Double.parseDouble(time) * 1000, Integer.parseInt(seed), output_trace);
		}
		if (solver == null) System.exit(1);

		List<Integer> res = solver.getResult();
		List<String> traces = solver.getTrace();

		StringBuilder sb = new StringBuilder();
		for (int num : res) { // assemble sol file
			sb.append(Integer.toString(num + 1));
			sb.append(", ");
		}

		System.out.println(sb.toString());
		if (!alg.equals("LS2")) { // print trace file
			for (String trace : traces) {
				trace_writer.println(trace);
				System.out.println(trace);
			}
		}
		sb.setLength(sb.length() - 2);
		sol_writer.println(res.size());
		sol_writer.println(sb.toString());
		sol_writer.close();
		trace_writer.close();
	}

	static Graph parseGraph(String graph_file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(graph_file));
		String line = br.readLine();
		String[] split = line.split(" ");
		int nodeNum = Integer.parseInt(split[0]);
		int edgeNum = Integer.parseInt(split[1]);
		Graph G = new Graph(nodeNum, edgeNum);
		for (int i = 0; i < nodeNum; i++) {
			G.nodes[i] = new ArrayList<>();
		}
		int lineIndex = 0;
		while ((line = br.readLine()) != null) {
			split = line.split(" ");
			for (String neighborS : split) {
				if (neighborS.equals("")) continue;
				int neighbor = Integer.parseInt(neighborS) - 1;
				if (neighbor > lineIndex) {
					Edge newEdge = new Edge(lineIndex, neighbor);
					G.nodes[lineIndex].add(newEdge);
					G.nodes[neighbor].add(newEdge);
					if (!G.edges.contains(newEdge))
						G.edges.add(newEdge);
				}
				if (!G.vertices.contains(lineIndex))
					G.vertices.add(lineIndex);
			}
			lineIndex++;
		}
		br.close();
		return G;
	}
}
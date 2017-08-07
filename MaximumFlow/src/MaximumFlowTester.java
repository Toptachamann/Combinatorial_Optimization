import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.util.ArrayList;

/**
 * Created by Timofey on 8/3/2017.
 */
public class MaximumFlowTester {
    public static boolean testMaximumFlow(String pathToGraph, String pathToLP, int numOfTestCases) {
        for (int i = 0; i < numOfTestCases; i++) {
            try {
                MaximumFlowSolver flowSolver = new MaximumFlowSolver();
                MaximumFlowTester.generateNetwork(pathToGraph, 10, 10, 50, 50, 10);
                flowSolver.readGraph(pathToGraph);
                BigDecimal maximumFlowSolverAns = new BigDecimal(String.valueOf(flowSolver.getMaximumFlow()));
                System.out.println("Solve by push-relabel algorithm, maximum flow value is: " + maximumFlowSolverAns);
                flowSolver.formulateLP(pathToLP);
                LPInputReader reader = new LPInputReader();
                reader.readInput(pathToLP);
                LPSolver solver = new LPSolver(reader.getLPStandardForm());
                //solver.setOut("D:\\Java_Projects\\CombinatorialOptimizationProblems\\src\\LPMaxFlowOutput.txt");
                solver.solve(50);
                BigDecimal lpSolverAns = solver.getOptimalObjectiveValue();
                System.out.println("Solved by linear programming solver, maximum objective function value is: " + lpSolverAns.toPlainString());
                if (lpSolverAns.compareTo(maximumFlowSolverAns) == 0) {
                    System.out.println("Answers match");
                } else {
                    System.out.println("Answers don't match");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void generateNetwork(String path, int vertLB, int vertUB, int edgeLB, int edgeUB, int capUB) throws IOException {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            int numOfVertices = (int) (Math.random() * (vertUB - vertLB) + vertLB);
            int numOfEdges = (int) (Math.random() * (edgeUB - edgeLB) + edgeLB);
            ArrayList<ArrayList<Integer>> graph = new ArrayList<>(numOfVertices);
            for (int i = 0; i < numOfVertices; i++) {
                ArrayList<Integer> temp = new ArrayList<>(numOfVertices);
                for (int j = 0; j < numOfVertices; j++) {
                    temp.add(0);
                }
                graph.add(temp);
            }
            for (int i = 0; i < numOfEdges; i++) {
                int u = (int) (Math.random() * numOfVertices);
                int v = u;
                while (v == u) {
                    v = (int) (Math.random() * numOfVertices);
                }
                int c = (int) (Math.random() * capUB);
                graph.get(u).set(v, c);
            }
            int s = (int) (Math.random() * numOfVertices) + 1;
            int t = s;
            while (t == s) {
                t = (int) (Math.random() * numOfVertices) + 1;
            }
            out.write(numOfVertices + " " + s + " " + t + "\n");
            for (int i = 0; i < numOfVertices; i++) {
                for (int j = 0; j < numOfVertices; j++) {
                    out.write(graph.get(i).get(j) + " ");
                }
                out.write("\n");
            }
            out.close();
        }
    }
}

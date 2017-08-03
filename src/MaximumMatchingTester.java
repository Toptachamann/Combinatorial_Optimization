import java.io.*;
import java.math.*;
import java.util.*;

/**
 * Created by Timofey on 7/31/2017.
 */
public class MaximumMatchingTester {
    public static boolean testMaximumMathching(String pathToGraph, String pathToLinearProgramme, int numOfTests) {
        File graphFile = new File(pathToGraph), lpFile = new File(pathToLinearProgramme);
        try {
            graphFile.createNewFile();
            lpFile.createNewFile();
            for(int i = 0; i < numOfTests; i++){
                MaximumMatchingTester.generateBipartiteGraph(pathToGraph, 500,600, 1300, 1500);
                //System.out.println("New graph is generated");
                MatchingSolver matchingSolver = new MatchingSolver();
                matchingSolver.readGraph(pathToGraph);
                BigDecimal matchingSolverAnswer = new BigDecimal(String.valueOf(matchingSolver.solveMaxMatching()));
                System.out.println("Solved by hungarian algorithm, the cardinality of the maximum matching is " + matchingSolverAnswer);
                matchingSolver.formulateLinearProgramme(lpFile.getAbsolutePath());
                //System.out.println("Linear programme is formulated");
                LPInputReader lpReader = new LPInputReader();
                lpReader.readInput(pathToLinearProgramme);
                //System.out.println("Linear programme is read");
                LPSolver lpSolver = new LPSolver(lpReader.getLPStandardForm(), false);
                //System.out.println("Starting simplex");
                lpSolver.solve(true, 50);
                BigDecimal lpSolverAnswer = lpSolver.getOptimalObjectiveValue();

                boolean checker = matchingSolverAnswer.compareTo(lpSolverAnswer) == 0 ? true : false;
                if (checker) {
                    System.out.println("The cardinality of the maximum matching is " + lpSolverAnswer.toPlainString() + ", both answers match\n");
                } else {
                    System.out.println("Answers don't match: lp solver answer: " + lpSolverAnswer.toPlainString() +
                            ", matching solver matcher answer: " + matchingSolverAnswer.toPlainString() + '\n');
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (SolutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class Edge implements Comparable<Edge>{
        private int u, v;
        public Edge(int u, int v){
            if(u < v){
                this.u = u;
                this.v = v;
            }else {
                this.u = v;
                this.v = u;
            }
        }
        @Override
        public int compareTo(Edge o) {
            if(u < o.u || v > o.v){
                return -1;
            }else if(u > o.u || v < o.v){
                return 1;
            }
            return 0;
        }
    }

    public static void generateBipartiteGraph(String path,  int vertexLowerBound,int vertexUpBound,  int edgeLowerBound ,int edgeUpBound) throws IOException {
        TreeSet<Edge> edgeSet = new TreeSet<>();
        PrintWriter out = new PrintWriter(path);
        int a = (int) (Math.random() * (vertexUpBound - vertexLowerBound) + vertexLowerBound),
                b = (int) (Math.random() * (vertexUpBound - vertexLowerBound) + vertexLowerBound);
        int edges = (int) (Math.random() * (edgeUpBound - edgeLowerBound) + edgeLowerBound);
        out.print(a + " " + b + " " + edges + "\n");
        for (int i = 0; i < edges; i++) {
            int u = (int) (Math.random() * a + 1);
            int v = (int) (Math.random() * b + 1);
            Edge edge = new Edge(u, v);
            if(!edgeSet.contains(edge)){
                out.print(u + " " + v + "\n");
                edgeSet.add(edge);
            }else{
                --i;
            }
        }
        out.close();
    }
}

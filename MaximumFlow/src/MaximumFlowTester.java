import java.math.*;

/**
 * Created by Timofey on 8/3/2017.
 */
public class MaximumFlowTester {
    public static boolean testMaximumFlow(String pathToGraph, String pathToLP, int numOfTestCases) {
        for (int i = 0; i < numOfTestCases; i++) {
            try {
                MaximumFlowSolver flowSolver = new MaximumFlowSolver();
                flowSolver.generateNetwork(pathToGraph, 10, 10, 50, 50, 10);
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
}

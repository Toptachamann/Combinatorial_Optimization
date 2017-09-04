import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Timofey on 8/9/2017.
 */
public class MinCostMatchingTester {
    public static void test(String pathToGraph, String pathToLP){
        try{
            MinCostMatchingSolver matchingSolver = new MinCostMatchingSolver();
            matchingSolver.readGraph(pathToGraph);
            matchingSolver.formulateInitialLP(pathToLP);

            LPInputReader reader = new LPInputReader();
            reader.readInput(pathToLP);
            LPSolver solver = new LPSolver(reader.getLPStandardForm(),
                    "D:\\Java_Projects\\CombinatorialOptimizationProblems\\MinCostMatching\\LPSolverOutput.txt");
            BigDecimal lpAnswer = solver.solve();
            int maxCardinalityMathing = lpAnswer.toBigInteger().intValue();
            matchingSolver.formulateFinalLP(pathToLP, maxCardinalityMathing);
            reader.readInput(pathToLP);
            solver.setLP(reader.getLPStandardForm());
            solver.setOut("D:\\Java_Projects\\CombinatorialOptimizationProblems\\MinCostMatching\\LPSolverOutput.txt");
            BigDecimal minCostMaxMatching = solver.solve();
            System.out.println("Solved by lp solver, the optimal cost of maximum cardinality matching is: " + minCostMaxMatching.toPlainString());
        }catch(IOException | SolutionException e){
            e.printStackTrace();
        }
    }
}

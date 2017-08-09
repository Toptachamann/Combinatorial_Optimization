import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by Timofey on 8/8/2017.
 */
public class MinCostFlowTester {
    public static void test(String pathToNetwork, String pathToLP){
        try{
            MinCostFlowSolver flowSolver = new MinCostFlowSolver();
            flowSolver.readGraph(pathToNetwork);
            flowSolver.formulateLP(pathToLP);

            LPInputReader reader = new LPInputReader();
            reader.readInput(pathToLP);
            LPSolver solver = new LPSolver(reader.getLPStandardForm(),
                    "D:\\Java_Projects\\CombinatorialOptimizationProblems\\MinimumCostFlow\\LPSolverOutput.txt");
            BigDecimal lpSolverAnswer = solver.solve();
            System.out.println("Solved by lp solver, the answer is " + lpSolverAnswer.negate().toPlainString());
        }catch(LPException e) {
            System.out.println(e.getMessage());
        }
        catch (IOException | SolutionException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }
    }
}

import java.io.IOException;

/**
 * Created by Timofey on 7/31/2017.
 */
public class Main {
    public static void main(String[] argc) {
        try{
            MaximumMatchingTester.testMaximumMatching("D:\\Java_Projects\\CombinatorialOptimizationProblems\\MaximumMatching\\src\\MatchingSolverInput.txt",
                    "D:\\Java_Projects\\CombinatorialOptimizationProblems\\MaximumMatching\\src\\MaximumMatchingAsLinearProgramme.txt", 10);
        }catch(IOException | SolutionException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
package org.stilab;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.BlockServiceLocator;
import org.stilab.utils.MetricsCalculator;
import org.stilab.utils.Pair;

public class App {
    @Parameter(names = {"-v", "--verbose"}, description = "Enable verbose logging")
    private boolean verbose;
    @Parameter(names = {"-f", "--file"}, description = "Path and name of file to use")
    private String file;
    @Parameter(names = {"-bic", "--bugInducingFile"}, description = "File Content of the Bug-Inducing Commit", required = true)
    private String bugInducingFile;
    @Parameter(names = {"-i", "--impactedLineIndex"}, description = "Index of the impacted Line", required = true)
    private int impactedLineIndex;
    @Parameter(names = {"-t", "--target"}, description = "Path target to save the generated measures", required = true)
    private String target;
    @Parameter(names = {"-h", "--help"}, description = "Help/Usage", help = true)
    private boolean help;

    private void processCommandLineArguments(final String[] arguments) {

        final JCommander commander = JCommander.newBuilder()
                                               .programName("Terraform Metrics Based-AST")
                                               .addObject(this).verbose(1).build();
        commander.parse(arguments);
        if (help) {
           commander.usage();
        }
        else {

          BlockServiceLocator blockServiceLocator = new BlockServiceLocator();

          try {
            Pair<BlockTreeImpl, String> identifiedBlockPair = blockServiceLocator.identifyRightBlock(bugInducingFile, impactedLineIndex);

            MetricsCalculator metricsCalculator = new MetricsCalculator();

            try {

              System.out.println(
                metricsCalculator.measureMetrics(
                  identifiedBlockPair.getFirst(),
                  identifiedBlockPair.getSecond())
              );

              metricsCalculator.writeMetricsAsJsonFile(
                identifiedBlockPair.getFirst(),
                identifiedBlockPair.getSecond(),
                target
                );

              System.out.println("Generated File metrics for the given Terraform Content at " +
                target);

            } catch (Exception e) {
              // Handle exceptions from
              // measureMetrics method
              System.err.println("Error while measuring metrics: " + e.getMessage());
            }
          } catch (Exception e) {
            // Handle exceptions from
            // identifyRightBlock method
            System.err.println("Error while identifying the right block: " + e.getMessage());
          }
        }
    }

    public static void main(final String[] arguments) {
        new App().processCommandLineArguments(arguments);
    }
}



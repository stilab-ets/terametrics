package org.stilab;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.*;

import java.util.List;

public class App {
    @Parameter(names = {"-f", "--file"}, description = "Path and name of file to use")
    private String file;
    @Parameter(names = {"-i", "--impactedLineIndex"}, description = "Index of the impacted Line")
    private int impactedLineIndex;
    @Parameter(names = {"-t", "--target"}, description = "Path target to save the generated measures", required = true)
    private String target;
    @Parameter(names = {"-h", "--help"}, description = "Help/Usage", help = true)
    private boolean help;
    @Parameter(names = {"-b", "--bloc"}, description = "Measure Quality Metrics for Tf Manifest Per Block")
    private boolean bloc;
    @Parameter(names = {"-l", "--locator"}, description = "Service Locator to localize the block by given His Unique Identifiers")
    private boolean locator;
    @Parameter(names = {"-bi","--blockIdentifier"}, description = "Block Identifier to the right blocks")
    private String blockIdentifier;
    @Parameter(names = {"-a", "--allBlocks"}, description = "Obtain all the positions of blocks declared in Tf File")
    private boolean allBlocks;

    private void processCommandLineArguments(final String[] arguments) {

        final JCommander commander = JCommander.newBuilder().programName("Terraform Metrics Based-AST").addObject(this).build();
        commander.parse(arguments);

        if (help) { commander.usage();}

        else {

          try {

              if (allBlocks) { // get the position of all the blocks [start-end]

                // Call the service locator to look for all the blocks identified in Tf file
                ServiceLocator serviceLocator = new ServiceLocator(file, target);
                // Save the positions as json objects
                List<JSONObject> objects = serviceLocator.obtainAllBlockPosition();
                // Put them as JSON file
                serviceLocator.saveJsonToFile(objects);
              }

              if (locator) { // look by block identifier and get the [start-end] indexes

                // Call the service locator to look for the identified block
                ServiceLocator serviceLocator = new ServiceLocator(blockIdentifier, file, target);
                // Save the positions as json objects
                List<JSONObject> objects = serviceLocator.saveIdentifiedBlocks();
                // Put them to JSON file
                serviceLocator.saveJsonToFile(objects);
              }

              if (bloc) { // Measure the metrics for the given script per block

                BlockDivider blockDivider = new BlockDivider(file);
                List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();
                MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);
                List<JSONObject> objects = metricsCalculatorBlocks.measureMetricsPerBlocks();

                //  Measure the metrics and generate A Json File for each one
                metricsCalculatorBlocks.saveJsonToFile(objects, target);

              }
          } catch (Exception e) {
             System.err.println("Error while identifying the right block: " + e.getMessage());
          }
        }
    }

    public static void main(final String[] arguments) {
        new App().processCommandLineArguments(arguments);
    }
}



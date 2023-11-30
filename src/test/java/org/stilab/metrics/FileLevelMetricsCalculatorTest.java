package org.stilab.metrics;

import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.parser.granularity.file.FileLevelMetricsCalculator;
import org.stilab.parser.spliters.BlockDivider;
import org.stilab.parser.mapper.BlockPosition;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileLevelMetricsCalculatorTest extends TestCase {

    private String hclFilePath;

    private String targetPathToSaveMetrics;

    @Override
    protected void setUp() throws Exception {

      super.setUp();

      hclFilePath = "src/test/java/org/stilab/metrics/data/base.tf";
      targetPathToSaveMetrics = "src/test/java/org/stilab/metrics/data/base_metrics.json";

      BlockDivider blockDivider = new BlockDivider(hclFilePath);
      List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();
      FileLevelMetricsCalculator fileLevelMetricsCalculator = new FileLevelMetricsCalculator(blockPositions);
      List<JSONObject> objects = fileLevelMetricsCalculator.measureMetricsPerBlocks();

      //  Measure the metrics and generate A Json File for each one
      fileLevelMetricsCalculator.saveJsonToFile(objects, targetPathToSaveMetrics);
    }

    public void testMainAttributesInMetricsFile() throws FileNotFoundException {
      JSONParser parser = new JSONParser();

      try {
        // Read the JSON file
        FileReader fileReader = new FileReader(targetPathToSaveMetrics);
        Object obj = parser.parse(fileReader);
        // Cast the parsed object to a JSONObject
        JSONObject jsonObject = (JSONObject) obj;
        // Get the "status" field
        long status = (long) jsonObject.get("status");

        // Get the head
        JSONObject head = (JSONObject) jsonObject.get("head");

        // Get the "num_lines_of_code" field
        long num_lines_of_code = (long) head.get("num_lines_of_code");
        // Get the "num_blocks" field
        long num_blocks = (long) head.get("num_blocks");
        // Get the "num_resources" field
        long num_resources = (long) head.get("num_resources");
        // Get the "num_modules" field
        long num_modules = (long) head.get("num_modules");
        // Get the "num_data" field
        long num_data = (long) head.get("num_data");
        // Get the "num_terraform" field
        long num_terraform = (long) head.get("num_terraform");
        // Get the "num_providers" field
        long num_providers = (long) head.get("num_providers");
        // Get the "num_variables" field
        long num_variables = (long) head.get("num_variables");
        // Get the "num_outputs" field
        long num_outputs = (long) head.get("num_outputs");
        // Get the "num_locals" field
        long num_locals = (long) head.get("num_locals");

        // Get the "data" array
        JSONArray dataArray = (JSONArray) jsonObject.get("data");
        int data_size = dataArray.size();

        assertEquals(status, 200);
        assertEquals(num_lines_of_code, 133);
        assertEquals(num_blocks, 1);
        assertEquals(num_resources, 1);
        assertEquals(num_modules, 0);

        assertEquals(num_data, 0);
        assertEquals(num_terraform, 0);
        assertEquals(num_providers, 0);
        assertEquals(num_variables, 0);
        assertEquals(num_outputs, 0);
        assertEquals(num_locals, 0);
        assertEquals(data_size, 1);

      } catch (IOException | ParseException e) {
        e.printStackTrace();
      }
    }

}

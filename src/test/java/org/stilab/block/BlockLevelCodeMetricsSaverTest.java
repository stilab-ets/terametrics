package org.stilab.block;

import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.stilab.utils.BlockDivider;
import org.stilab.utils.BlockPosition;
import org.stilab.utils.MetricsCalculatorBlocks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BlockLevelCodeMetricsSaverTest extends TestCase {

    private String hclFilePath;

    private String targetPathToSaveMetrics;

    @Override
    protected void setUp() throws Exception {

      super.setUp();

      hclFilePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\base.tf";
      targetPathToSaveMetrics = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\base_metrics.json";

      BlockDivider blockDivider = new BlockDivider(hclFilePath);
      List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();
      MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);
      List<JSONObject> objects = metricsCalculatorBlocks.measureMetricsPerBlocks();

      //  Measure the metrics and generate A Json File for each one
      metricsCalculatorBlocks.saveJsonToFile(objects, targetPathToSaveMetrics);
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
        // Get the "data" array
        JSONArray dataArray = (JSONArray) jsonObject.get("data");
        int data_size = dataArray.size();

        assertEquals(status, 200);
        assertEquals(num_lines_of_code, 132);
        assertEquals(num_blocks, 1);
        assertEquals(data_size, 1);

      } catch (IOException | ParseException e) {
        e.printStackTrace();
      }
    }

}
package org.stilab.metrics.counter.block;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.mapper.BlockPosition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MetricsCalculatorBlocks {

      private static final Logger logger = Logger.getLogger(MetricsCalculatorBlocks.class.getName());

      private List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions;

      public MetricsCalculatorBlocks(List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions) {
        this.blockPositions = blockPositions;
      }
      public List<JSONObject> measureMetricsPerBlocks() {
          BlockMetricsCalculator blockMetricsCalculator = new BlockMetricsCalculator();
          List<JSONObject> objects = new ArrayList<>();
          for (BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> blockPosition: this.blockPositions) {
            JSONObject jsonObject  = blockMetricsCalculator.measureMetrics(
                 (BlockTreeImpl) blockPosition.getObject(),
                (String) blockPosition.getContent()
            );
            objects.add(jsonObject);
          }
        return objects;
      }

      public JSONObject getHead(JSONArray jsonArray) {
        JSONObject head = new JSONObject();
        head.put("num_blocks", jsonArray.size());
        int fileLoc = 0;
        // Loop using a simple for loop
        for (int i = 0; i < jsonArray.size(); i++) {
          JSONObject jsonObject = (JSONObject) jsonArray.get(i);
          int blockLoc = (int) jsonObject.get("loc");
          fileLoc = blockLoc + fileLoc;
        }
        head.put("num_lines_of_code", fileLoc);
        return head;
      }

      public void saveJsonToFile(List<JSONObject> jsonList, String filePath) {
        JSONArray jsonArray = new JSONArray();
        int status = 200;
        if (jsonList.isEmpty()){
          status = 404;
        }
        for (JSONObject jsonObject : jsonList) {
          jsonArray.add(jsonObject);
        }
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("head", getHead(jsonArray));
        result.put("data", jsonArray);
        try (FileWriter fileWriter = new FileWriter(filePath)) {
          fileWriter.write(result.toString());
          logger.info(String.format("JSON objects saved to file: %s", filePath));
        } catch (IOException e) {
          logger.severe(String.format("Error while saving JSON objects to file: %s", e.getMessage()));
        }
      }
}

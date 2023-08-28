package org.stilab.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetricsCalculatorBlocks {
      private List<BlockPosition> blockPositions;
      public MetricsCalculatorBlocks(List<BlockPosition> blockPositions) {
        this.blockPositions = blockPositions;
      }
      public List<JSONObject> measureMetricsPerBlocks() {
          MetricsCalculator metricsCalculator = new MetricsCalculator();
          List<JSONObject> objects = new ArrayList<>();
          for (BlockPosition blockPosition: this.blockPositions) {
            JSONObject jsonObject  = metricsCalculator.measureMetrics(
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
          System.out.println("JSON objects saved to file: " + filePath);
        } catch (IOException e) {
          System.err.println("Error while saving JSON objects to file: " + e.getMessage());
        }
      }
}

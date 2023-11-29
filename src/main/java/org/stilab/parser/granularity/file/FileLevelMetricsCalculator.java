package org.stilab.parser.granularity.file;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.parser.granularity.block.BlockLevelMetricsCalculator;
import org.stilab.parser.mapper.BlockPosition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLevelMetricsCalculator {

      private static final Logger logger = Logger.getLogger(FileLevelMetricsCalculator.class.getName());

      private List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions;

      public FileLevelMetricsCalculator(List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions) {
        this.blockPositions = blockPositions;
      }
      public List<JSONObject> measureMetricsPerBlocks() {
          BlockLevelMetricsCalculator blockLevelMetricsCalculator = new BlockLevelMetricsCalculator();
          List<JSONObject> objects = new ArrayList<>();
          for (BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> blockPosition: this.blockPositions) {
            JSONObject jsonObject  = blockLevelMetricsCalculator.measureMetrics(blockPosition.getObject(), blockPosition.getContent()
            );
            objects.add(jsonObject);
          }
        return objects;
      }



      public JSONObject getHead(JSONArray jsonArray) {
        JSONObject head = new JSONObject();
        head.put("num_blocks", jsonArray.size());
        int fileLoc = 0;
        // Loop using a simple
        // for loop
        int isResource = 0;
        int isModule = 0;
        int isData = 0;
        int isTerraform = 0;
        int isProvider = 0;
        int isVariable =  0;
        int isOutput =  0;
        int isLocals =  0;

        for (int i = 0; i < jsonArray.size(); i++) {
          JSONObject jsonObject = (JSONObject) jsonArray.get(i);
          int blockLoc = (int) jsonObject.get("loc");

          isResource += (int) jsonObject.get("isResource");
          isModule += (int) jsonObject.get("isModule");
          isData += (int) jsonObject.get("isData");
          isTerraform += (int) jsonObject.get("isTerraform");
          isProvider += (int) jsonObject.get("isProvider");
          isVariable += (int) jsonObject.get("isVariable");
          isOutput += (int) jsonObject.get("isOutput");
          isLocals += (int) jsonObject.get("isLocals");

          fileLoc = blockLoc + fileLoc;
        }

        head.put("num_lines_of_code", fileLoc);
        head.put("num_resources", isResource);
        head.put("num_modules", isModule);
        head.put("num_data", isData);
        head.put("num_terraform", isTerraform);
        head.put("num_providers", isProvider);
        head.put("num_variables", isVariable);
        head.put("num_outputs", isOutput);
        head.put("num_locals", isLocals);
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
          logger.log(Level.INFO, "JSON objects saved to file {0}", filePath);
        } catch (IOException e) {
          logger.severe(String.format("Error while saving JSON objects to file: %s", e.getMessage()));
        }
      }
}

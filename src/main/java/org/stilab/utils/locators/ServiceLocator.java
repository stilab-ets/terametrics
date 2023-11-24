package org.stilab.utils.locators;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.spliters.BlockDivider;
import org.stilab.utils.mapper.BlockPosition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceLocator {
    private static final Logger logger = Logger.getLogger(ServiceLocator.class.getName());
    private String blockIdentifier;
    private final String filePath;
    private final String target;

    public ServiceLocator(String filePath, String target) {
      this.filePath = filePath;
      this.target = target;
    }

    public ServiceLocator(String blockIdentifier, String filePath, String target) {
        this.blockIdentifier = blockIdentifier;
        this.filePath = filePath;
        this.target = target;
    }

    public List<JSONObject> obtainAllBlockPosition(){
      BlockDivider blockDivider = new BlockDivider(filePath);
      List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();

      List<JSONObject> objects = new ArrayList<>();
      for (BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> blockPosition: blockPositions) {
        objects.add(blockPosition.toJson());
      }
      return objects;
    }

    public List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> getRightBlocks() {
        BlockDivider blockDivider = new BlockDivider(filePath);
        List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();
        List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> filteredBlocks = new ArrayList<>();
        for(BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> position: blockPositions) {
          if (position.getIdentifier().equals(this.blockIdentifier)) {
            filteredBlocks.add(position);
          }
        }
        return filteredBlocks;
    }

    public List<JSONObject> saveIdentifiedBlocks() {

      List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = this.getRightBlocks();
      List<JSONObject> objects = new ArrayList<>();
      for (BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> blockPosition: blockPositions) {
        objects.add(blockPosition.toJson());
      }
      return objects;
    }

    public void saveJsonToFile(List<JSONObject> jsonList) {

      JSONArray jsonArray = new JSONArray();
      for (JSONObject jsonObject : jsonList) {
        jsonArray.add(jsonObject);
      }
      try (FileWriter fileWriter = new FileWriter(target)) {
        fileWriter.write(jsonArray.toString());
        logger.log(Level.INFO, "JSON objects saved to file {0} ", target);
      } catch (IOException e) {
        logger.severe(String.format("Error while saving JSON objects to file: %s", e.getMessage()));
      }
    }
}

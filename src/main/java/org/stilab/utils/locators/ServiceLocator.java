package org.stilab.utils.locators;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.stilab.utils.spliters.BlockDivider;
import org.stilab.utils.mapper.BlockPosition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceLocator {

    private String blockIdentifier;
    private String filePath;
    private String target;

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
      List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();

      List<JSONObject> objects = new ArrayList<>();
      for (BlockPosition blockPosition: blockPositions) {
        objects.add(blockPosition.toJson());
      }
      return objects;
    }

    public List<BlockPosition> getRightBlocks() {
        BlockDivider blockDivider = new BlockDivider(filePath);
        List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();
        List<BlockPosition> filteredBlocks = new ArrayList<>();
        for(BlockPosition position: blockPositions) {
          if (position.getIdentifier().equals(this.blockIdentifier)) {
            filteredBlocks.add(position);
          }
        }
        return filteredBlocks;
    }

    public List<JSONObject> saveIdentifiedBlocks() {

      List<BlockPosition> blockPositions = this.getRightBlocks();
      List<JSONObject> objects = new ArrayList<>();
      for (BlockPosition blockPosition: blockPositions) {
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
        System.out.println("JSON objects saved to file: " + target);
      } catch (IOException e) {
        System.err.println("Error while saving JSON objects to file: " + e.getMessage());
      }
    }

    public String getBlockIdentifier() {
      return blockIdentifier;
    }
}

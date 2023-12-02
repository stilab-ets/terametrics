package org.stilab.parser.granularity.dir;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.parser.granularity.file.FileLevelMetricsCalculator;
import org.stilab.parser.mapper.BlockPosition;
import org.stilab.parser.spliters.BlockDivider;
import org.stilab.parser.spliters.DirAnalyzerService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RepoAnalyzer {

      protected String LOCAL_PATH;
      protected String full_name_repo;
      protected String TARGET_FILE;
      protected DirAnalyzerService dirAnalyzerService;

      public RepoAnalyzer(String full_name_repo, String absoluteLocalPath, String targetFile) {
              this.TARGET_FILE = targetFile;
              this.LOCAL_PATH = absoluteLocalPath;
              this.full_name_repo = full_name_repo;
              dirAnalyzerService = new DirAnalyzerService();
      }

      protected List<String> getTfFilesList() {
        return null;
      }


      private JSONObject createJsonObject(List<String> tfFiles) {
        JSONObject root = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (String tfFile : tfFiles) {
          BlockDivider blockDivider = new BlockDivider(tfFile);
          List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();
          FileLevelMetricsCalculator fileLevelMetricsCalculator = new FileLevelMetricsCalculator(blockPositions);
          List<JSONObject> objects = fileLevelMetricsCalculator.measureMetricsPerBlocks();

          JSONObject object = new JSONObject();
          object.put("file", tfFile);
          object.put("blocks", objects);
          jsonArray.add(object);
        }

        root.put("project", this.full_name_repo);
        root.put("data", jsonArray);

        return root;
      }

      private void saveJsonObjectToFile(JSONObject jsonObject) {
        try (FileWriter fileWriter = new FileWriter(this.TARGET_FILE)) {
          fileWriter.write(jsonObject.toString());
          System.out.println("JSON objects saved to file: " + this.TARGET_FILE);
        } catch (IOException e) {
          System.err.println("Error while saving JSON objects to file: " + e.getMessage());
        }
      }

      public JSONObject analyzeTfFiles() {
        List<String> tfFiles = this.getTfFilesList();
        JSONObject jsonObject = createJsonObject(tfFiles);
        saveJsonObjectToFile(jsonObject);
        return jsonObject;
      }

}

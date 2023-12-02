package org.stilab.parser.facade;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.parser.granularity.file.FileLevelMetricsCalculator;
import org.stilab.parser.mapper.BlockPosition;
import org.stilab.parser.spliters.BlockDivider;

import java.util.List;

public class FileCommand implements Command{

      private String filePath;
      private String target;

      public FileCommand(String filePath, String target){
        this.filePath = filePath;
        this.target = target;
      }

      @Override
      public void execute() {

        BlockDivider blockDivider = new BlockDivider(filePath);
        List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();

        FileLevelMetricsCalculator fileLevelMetricsCalculator = new FileLevelMetricsCalculator(blockPositions);
        List<JSONObject> objects = fileLevelMetricsCalculator.measureMetricsPerBlocks();

        //  Measure the metrics and generate A Json File for each one
        fileLevelMetricsCalculator.saveJsonToFile(objects, target);
      }

}

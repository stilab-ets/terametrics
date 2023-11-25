package org.stilab.facade;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.FileLevelMetricsCalculator;
import org.stilab.utils.mapper.BlockPosition;
import org.stilab.utils.spliters.BlockDivider;

import java.util.List;

public class FileCommand implements Command{
  @Override
  public void execute(String file, String target) {

    BlockDivider blockDivider = new BlockDivider(file);
    List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();

    FileLevelMetricsCalculator fileLevelMetricsCalculator = new FileLevelMetricsCalculator(blockPositions);
    List<JSONObject> objects = fileLevelMetricsCalculator.measureMetricsPerBlocks();

    //  Measure the metrics and generate A Json File for each one
    fileLevelMetricsCalculator.saveJsonToFile(objects, target);
  }
}

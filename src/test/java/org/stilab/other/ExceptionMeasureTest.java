package org.stilab.other;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.stilab.utils.BlockDivider;
import org.stilab.utils.BlockPosition;
import org.stilab.utils.MetricsCalculatorBlocks;

import java.util.List;

public class ExceptionMeasureTest extends TestCase {


    public void testMetricGenerationInCaseOfException() {
      String file = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tmp_blob.tf";
      String target = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\metrics_test.json";
      BlockDivider blockDivider = new BlockDivider(file);
      List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();
      MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);
      List<JSONObject> objects = metricsCalculatorBlocks.measureMetricsPerBlocks();
      //  Measure the metrics and generate A Json File for each one
      metricsCalculatorBlocks.saveJsonToFile(objects, target);
    }

}

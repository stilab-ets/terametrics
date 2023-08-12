package org.stilab.other;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.stilab.utils.BlockDivider;
import org.stilab.utils.BlockPosition;
import org.stilab.utils.MetricsCalculatorBlocks;

import java.util.List;

public class BlockDividerBlock extends TestCase {

      public void testDividingBlock() {

        BlockDivider blockDivider = new BlockDivider("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner" +
          "\\src\\test\\java\\org\\stilab\\tmp_blob.tf");

        List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();

        MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);

        assertEquals(23, metricsCalculatorBlocks.measureMetricsPerBlocks().size());

      }


      public void testMeasureMetrics() {
        // String file -->
        String file = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner" +
          "\\src\\test\\java\\org\\stilab\\tmp_blob.tf";
        String target = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner" +
          "\\src\\test\\java\\org\\stilab\\metrics_block.json";

        BlockDivider blockDivider = new BlockDivider(file);
        List<BlockPosition> blockPositions = blockDivider.divideFilePerBlock();
        MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);
        List<JSONObject> objects = metricsCalculatorBlocks.measureMetricsPerBlocks();

        //  Measure the metrics and generate A Json File for each one
        metricsCalculatorBlocks.saveJsonToFile(objects, target);

      }

}

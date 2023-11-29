package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DynamicBlocksVisitor;

public class DynamicBlocksCollector implements Repository {


    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      DynamicBlocksVisitor dynamicBlocksVisitor = new DynamicBlocksVisitor();
      dynamicBlocksVisitor.filterDynamicBlock(identifiedBlock);
      int numDynamicBlock = dynamicBlocksVisitor.countDynamicBlock();
      metrics.put("numDynamicBlocks", numDynamicBlock);
      return metrics;

    }
}

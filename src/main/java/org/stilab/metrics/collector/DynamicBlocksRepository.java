package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.DynamicBlocksVisitor;

public class DynamicBlocksRepository implements Repository {


    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      DynamicBlocksVisitor dynamicBlocksVisitor = new DynamicBlocksVisitor();
      dynamicBlocksVisitor.filterDynamicBlock(identifiedBlock);
      int numDynamicBlock = dynamicBlocksVisitor.countDynamicBlock();
      metrics.put("numDynamicBlocks", numDynamicBlock);
      return metrics;

    }
}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import java.util.ArrayList;
import java.util.List;

public class DynamicBlocksIdentifier {

    List<BlockTreeImpl> dynamicBlock = new ArrayList<>();

    public boolean isDynamicBlock(BlockTreeImpl nestedBlock) {
      return nestedBlock.key().value().equals("dynamic");
    }

    public List<BlockTreeImpl> filterDynamicBlock(BlockTreeImpl parentBlock) {

      NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
      List<BlockTreeImpl> blockTreeList = new ArrayList<>();
      blockTreeList.addAll(nestedBlockCounter.getAllNestedBlocks(parentBlock));

      for (BlockTreeImpl blockTree: blockTreeList) {
          if ( isDynamicBlock(blockTree) ) {
            dynamicBlock.add(blockTree);
          }
      }
      return dynamicBlock;
    }

    public int countDynamicBlock() {
      return this.dynamicBlock.size();
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterDynamicBlock(identifiedBlock);
      int numDynamicBlock = this.countDynamicBlock();

      metrics.put("numDynamicBlocks", numDynamicBlock);

      return metrics;
    }
}

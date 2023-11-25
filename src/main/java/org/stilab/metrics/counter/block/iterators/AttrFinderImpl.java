package org.stilab.metrics.counter.block.iterators;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.NestedBlockVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttrFinderImpl {
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public AttrFinderImpl() {
      //  Constructor
    }

    // You should add also for the top attributes
    public List<AttributeTreeImpl> getAttributes(BlockTreeImpl nested) {

      return nested.value().statements()
        .stream()
        .filter(AttributeTreeImpl.class::isInstance)
        .map(AttributeTreeImpl.class::cast)
        .collect(Collectors.toList());
    }

    // You should add also for the all attributes (tops + blocks)
    public List<AttributeTreeImpl> getAllAttributes(BlockTreeImpl rootBlock) {
      NestedBlockVisitor nestedBlockCounter = new NestedBlockVisitor();
      List<BlockTreeImpl> blockTreeList = new ArrayList<>();
      blockTreeList.addAll(nestedBlockCounter.getAllNestedBlocks(rootBlock));
      // loop over the blocks
      for (BlockTreeImpl blockTree: blockTreeList) {
        attributes.addAll( getAttributes(blockTree) );
      }
      return attributes;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      metrics.put("numAttrs", this.getAllAttributes(identifiedBlock).size());
      return metrics;
    }
}

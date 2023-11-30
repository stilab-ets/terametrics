package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.NestedBlockCalculator;
import org.stilab.visitors.NestedBlockVisitor;

import java.util.List;

public class NestedBlockCollector implements Decorator {
  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

//    NestedBlockVisitor nestedBlockVisitor = new NestedBlockVisitor();
//
//    List<BlockTreeImpl> nstdBlocks = nestedBlockVisitor.identifyNestedBlock(identifiedBlock);
//    int numNestedBlocks = nestedBlockVisitor.countNestedBlock();


    NestedBlockCalculator nestedBlockCalculator = new NestedBlockCalculator(identifiedBlock);

    metrics.put("numNestedBlocks", nestedBlockCalculator.countNestedBlock());
    metrics.put("avgDepthNestedBlocks", nestedBlockCalculator.avgDepthNestedBlocks());
    metrics.put("maxDepthNestedBlocks", nestedBlockCalculator.maxDepthNestedBlocks());
    metrics.put("minDepthNestedBlocks", nestedBlockCalculator.minDepthNestedBlocks());

    return metrics;
  }
}

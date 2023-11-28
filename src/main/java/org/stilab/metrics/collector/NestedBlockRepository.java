package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.NestedBlockVisitor;

import java.util.List;

public class NestedBlockRepository implements Repository{
  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    NestedBlockVisitor nestedBlockVisitor = new NestedBlockVisitor();

    List<BlockTreeImpl> nstdBlocks = nestedBlockVisitor.identifyNestedBlock(identifiedBlock);
    int numNestedBlocks = nestedBlockVisitor.countNestedBlock();

    metrics.put("numNestedBlocks", numNestedBlocks);
    metrics.put("avgDepthNestedBlocks", nestedBlockVisitor.avgDepthNestedBlocks(nstdBlocks));
    metrics.put("maxDepthNestedBlocks", nestedBlockVisitor.maxDepthNestedBlocks(nstdBlocks));
    metrics.put("minDepthNestedBlocks", nestedBlockVisitor.minDepthNestedBlocks(nstdBlocks));

    return metrics;
  }
}

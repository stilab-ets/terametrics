package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.BlockLabelIdentifier;
import java.util.List;

public class BlockMetaInfo {

  public BlockMetaInfo(){}

  public String concatElementsOfList(List<String> stringList) {
    return String.join(" ", stringList);
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
    List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(identifiedBlock);

    metrics.put("impacted_block_type", this.concatElementsOfList(labels));
    metrics.put("block", identifiedBlock.key().value());
    metrics.put("start_block", identifiedBlock.key().textRange().start().line());
    metrics.put("end_block", identifiedBlock.value().textRange().end().line());
    labels.add(0, identifiedBlock.key().value());
    metrics.put("block_identifiers", this.concatElementsOfList(labels));

    return metrics;
  }
}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.BlockLabelIdentifier;
import java.util.List;

public class BlockMetaInfo {

    private final String IMPACTED_BLOCK = "impacted_block_type";
    private final String BLOCK_TYPE = "block";
    private final String START_BLOCK = "start_block";
    private final String END_BLOCK = "end_block";
    private final String BLOCK_IDENTIFIER = "block_identifiers";
    private final String BLOCK_ID = "block_id";
    private final String BLOCK_NAME = "block_name";

    public String concatElementsOfList(List<String> stringList) {
      return String.join(" ", stringList);
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
      List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(identifiedBlock);

      metrics.put(IMPACTED_BLOCK, this.concatElementsOfList(labels));
      metrics.put(BLOCK_TYPE, identifiedBlock.key().value());
      metrics.put(START_BLOCK, identifiedBlock.key().textRange().start().line());
      metrics.put(END_BLOCK, identifiedBlock.value().textRange().end().line());
      labels.add(0, identifiedBlock.key().value());
      metrics.put(BLOCK_IDENTIFIER, this.concatElementsOfList(labels));

      if (labels.size() == 3)  {
        metrics.put(BLOCK_ID,   labels.get(1));
        metrics.put(BLOCK_NAME, labels.get(2));

      }

      if (labels.size() == 2) {
        metrics.put(BLOCK_ID, "");
        metrics.put(BLOCK_NAME, labels.get(1));
      }

      if (labels.size() == 1) {
        metrics.put(BLOCK_ID, "");
        metrics.put(BLOCK_NAME, "");
      }

      return metrics;
    }
}

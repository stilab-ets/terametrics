package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.BlockLabelVisitor;

import java.util.List;

public class BlockMetaInfoCollector implements Repository {
    private static final String IMPACTED_BLOCK_TYPE = "impacted_block_type";
    private static final String BLOCK_TYPE = "block";
    private static final String START_BLOCK = "start_block";
    private static final String END_BLOCK = "end_block";
    private static final String BLOCK_IDENTIFIERS = "block_identifiers";
    private static final String BLOCK_ID = "block_id";
    private static final String BLOCK_NAME = "block_name";

    public String concatElementsOfList(List<String> stringList) {
      return String.join(" ", stringList);
    }

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      BlockLabelVisitor blockLabelVisitor = new BlockLabelVisitor();
      List<String> labels = blockLabelVisitor.identifyLabelsOfBlock(identifiedBlock);

      metrics.put(IMPACTED_BLOCK_TYPE, this.concatElementsOfList(labels));
      metrics.put(BLOCK_TYPE, identifiedBlock.key().value());
      metrics.put(START_BLOCK, identifiedBlock.key().textRange().start().line());
      metrics.put(END_BLOCK, identifiedBlock.value().textRange().end().line());
      labels.add(0, identifiedBlock.key().value());
      metrics.put(BLOCK_IDENTIFIERS, this.concatElementsOfList(labels));

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

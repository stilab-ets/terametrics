package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.BlockLabelIdentifier;
import java.util.List;

public class BlockMetaInfo {

    private final static String impactedBlockType = "impacted_block_type";
    private final static String blockType = "block";
    private final static String startBlock = "start_block";
    private final static String endBlock = "end_block";
    private final static String blockIdentifiers = "block_identifiers";
    private final static String blockId = "block_id";
    private final static String blockName = "block_name";

    public String concatElementsOfList(List<String> stringList) {
      return String.join(" ", stringList);
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
      List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(identifiedBlock);

      metrics.put(impactedBlockType, this.concatElementsOfList(labels));
      metrics.put(blockType, identifiedBlock.key().value());
      metrics.put(startBlock, identifiedBlock.key().textRange().start().line());
      metrics.put(endBlock, identifiedBlock.value().textRange().end().line());
      labels.add(0, identifiedBlock.key().value());
      metrics.put(blockIdentifiers, this.concatElementsOfList(labels));

      if (labels.size() == 3)  {
        metrics.put(blockId,   labels.get(1));
        metrics.put(blockName, labels.get(2));

      }

      if (labels.size() == 2) {
        metrics.put(blockId, "");
        metrics.put(blockName, labels.get(1));
      }

      if (labels.size() == 1) {
        metrics.put(blockId, "");
        metrics.put(blockName, "");
      }

      return metrics;
    }
}

package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import java.util.List;

public class EmptyStringIdentifier {
    private LiteralExpressionIdentifier literalExpressionIdentifier;

    public EmptyStringIdentifier(LiteralExpressionIdentifier literalExpressionIdentifier) {
      this.literalExpressionIdentifier = literalExpressionIdentifier;
    }

    public int numberOfEmptyString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(identifiedBlock);
      int numEmptyString = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
          if (literalExpressionIdentifier.isLiteralExprValueString(literalExprTree)) {
            if (literalExprTree.value().isEmpty()) {
              numEmptyString += 1;
            }
          }
      }
      return numEmptyString;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      metrics.put("numEmptyString", numberOfEmptyString(identifiedBlock));
      return metrics;
    }
}

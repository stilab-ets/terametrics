package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import java.util.List;

public class SpecialStringIdentifier {
    private LiteralExpressionIdentifier literalExpressionIdentifier;

    public SpecialStringIdentifier(LiteralExpressionIdentifier literalExpressionIdentifier) {
      this.literalExpressionIdentifier = literalExpressionIdentifier;
    }

    public int numberOfEmptyString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(identifiedBlock);
      int numEmptyString = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
          if (literalExpressionIdentifier.isLiteralExprValueString(literalExprTree) && literalExprTree.value().isEmpty()) {
              numEmptyString += 1;
          }
      }
      return numEmptyString;
    }

    public int numberOfWildCardSuffixString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(identifiedBlock);
      int numWildCardSuffix = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        if (literalExpressionIdentifier.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals(":*")) {
            numWildCardSuffix += 1;
        }
      }
      return numWildCardSuffix;
    }

    public int numberOfStarString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(identifiedBlock);
      int numStar = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        if (literalExpressionIdentifier.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals("*")) {
            numStar += 1;
        }
      }
      return numStar;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      metrics.put("numEmptyString", numberOfEmptyString(identifiedBlock));
      metrics.put("numWildCardSuffixString", numberOfWildCardSuffixString(identifiedBlock));
      metrics.put("numStarString", numberOfStarString(identifiedBlock));
      return metrics;
    }
}

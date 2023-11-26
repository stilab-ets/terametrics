package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import java.util.List;

public class SpecialStringVisitor {
    private LiteralExpressionVisitor literalExpressionVisitor;

    public SpecialStringVisitor(LiteralExpressionVisitor literalExpressionVisitor) {
      this.literalExpressionVisitor = literalExpressionVisitor;
    }

    public int numberOfEmptyString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);
      int numEmptyString = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
          if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().isEmpty()) {
              numEmptyString += 1;
          }
      }
      return numEmptyString;
    }

    public int numberOfWildCardSuffixString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);
      int numWildCardSuffix = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals(":*")) {
            numWildCardSuffix += 1;
        }
      }
      return numWildCardSuffix;
    }

    public int numberOfStarString(BlockTreeImpl identifiedBlock){
      List<LiteralExprTreeImpl> literalExprTrees = literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);
      int numStar = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals("*")) {
            numStar += 1;
        }
      }
      return numStar;
    }

}

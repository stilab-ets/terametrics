package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.visitors.LiteralExpressionVisitor;
import org.stilab.visitors.SpecialStringVisitor;

import java.util.List;

public class SpecialStringCalculator  {


  private LiteralExpressionVisitor literalExpressionVisitor;

  public SpecialStringCalculator(BlockTreeImpl identifiedBlock) {
    literalExpressionVisitor = new LiteralExpressionVisitor();
    literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);
  }

  public int numberOfEmptyString(){

    int numEmptyString = 0;
    for (LiteralExprTreeImpl literalExprTree: literalExpressionVisitor.getLiteralExprTrees()) {
      if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().isEmpty()) {
        numEmptyString += 1;
      }
    }
    return numEmptyString;
  }

  public int numberOfWildCardSuffixString(){
    int numWildCardSuffix = 0;
    for (LiteralExprTreeImpl literalExprTree: literalExpressionVisitor.getLiteralExprTrees()) {
      if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals(":*")) {
        numWildCardSuffix += 1;
      }
    }
    return numWildCardSuffix;
  }

  public int numberOfStarString(){
    int numStar = 0;
    for (LiteralExprTreeImpl literalExprTree: literalExpressionVisitor.getLiteralExprTrees()) {
      if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree) && literalExprTree.value().equals("*")) {
        numStar += 1;
      }
    }
    return numStar;
  }

}

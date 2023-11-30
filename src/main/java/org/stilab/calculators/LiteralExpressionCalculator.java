package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.visitors.LiteralExpressionVisitor;

public class LiteralExpressionCalculator {


  private LiteralExpressionVisitor literalExpressionVisitor;

  public LiteralExpressionCalculator(BlockTreeImpl identifiedBlock){
    literalExpressionVisitor = new LiteralExpressionVisitor();
    literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);
  }

  public int totalNumberOfLiteralExpressions(){
    return literalExpressionVisitor.getLiteralExprTrees().size();
  }

  public int numStringValues(){
    int sum = 0;
    for (LiteralExprTreeImpl literalExprTree: literalExpressionVisitor.getLiteralExprTrees()) {
      if (literalExpressionVisitor.isLiteralExprValueString(literalExprTree)) {
        sum += 1;
      }
    }
    return sum;
  }


}

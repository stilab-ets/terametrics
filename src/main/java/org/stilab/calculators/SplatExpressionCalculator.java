package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.SplatExpressionVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SplatExpressionCalculator {

  private SplatExpressionVisitor splatExpressionVisitor;
  public SplatExpressionCalculator(BlockTreeImpl identifiedBlock) {
    splatExpressionVisitor = new SplatExpressionVisitor();
    splatExpressionVisitor.filtersSplatsFromBlock(identifiedBlock);
  }

  public int totalSplatExpressions() {
    return splatExpressionVisitor.getSplatExpressions().size();
  }

  public double avgSplatExpressions() {
    if (!splatExpressionVisitor.getAttributes().isEmpty()) {
      double avgSplatExpressions = (double) totalSplatExpressions() / splatExpressionVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgSplatExpressions).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxSplatExpressions() {

    if (splatExpressionVisitor.getAttributes().isEmpty()) { return 0;}

    int max = splatExpressionVisitor.visit(splatExpressionVisitor.getAttributes().get(0)).size();

    for(AttributeTreeImpl attribute: splatExpressionVisitor.getAttributes()) {
      int value = splatExpressionVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }

    return max;
  }
}

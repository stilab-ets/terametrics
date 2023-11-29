package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.TemplateExpressionVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TemplateExpressionCalculator  {

  private TemplateExpressionVisitor templateExpressionVisitor;

  public TemplateExpressionCalculator(BlockTreeImpl blockTree) {
    templateExpressionVisitor = new TemplateExpressionVisitor();
    templateExpressionVisitor.filterTemplateExpressionsFromBlock(blockTree);
  }
  public int totalNumberOfTemplateExpressionsPerBlock() {
    return templateExpressionVisitor.getTemplateExpressionPerBlock().size();
  }

  public double avgNumOfTemplateExpressionPerBlock() {
    if (!templateExpressionVisitor.getAttributes().isEmpty()) {
      double avgNumOfTemplateExpressionPerBlock = (double) totalNumberOfTemplateExpressionsPerBlock() / templateExpressionVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumOfTemplateExpressionPerBlock).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

}

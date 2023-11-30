package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.IndexAccessVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IndexAccessCalculator {

  private IndexAccessVisitor indexAccessVisitor;

  public IndexAccessCalculator(BlockTreeImpl identifiedBlock) {
    indexAccessVisitor = new IndexAccessVisitor();
    indexAccessVisitor.identifyIndexAccessFromBlock(identifiedBlock);
  }

  public int totalIndexAccessExpressions() {
    return indexAccessVisitor.getIndexAccess().size();
  }

  public double avgIndexAccessExpressions() {
    if (!indexAccessVisitor.getAttributes().isEmpty()){
      double avgIndexAccessExpressions = (double) totalIndexAccessExpressions() / indexAccessVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgIndexAccessExpressions).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxIndexAccessExpressions() {
    if (indexAccessVisitor.getAttributes().isEmpty()){ return 0; }

    int max = indexAccessVisitor.visit(indexAccessVisitor.getAttributes().get(0)).size();
    for (AttributeTreeImpl attribute: indexAccessVisitor.getAttributes()) {
      int value = indexAccessVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }

    return max;
  }



}

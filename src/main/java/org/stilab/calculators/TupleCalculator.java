package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.TupleVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TupleCalculator {

  private TupleVisitor tupleVisitor;
  public TupleCalculator(BlockTreeImpl identifiedBlock) {
    tupleVisitor = new TupleVisitor();
    tupleVisitor.filterTuplesFromBlock(identifiedBlock);
  }

  public int totalNumberOfTuples() {
    return this.tupleVisitor.getTuples().size();
  }

  public double avgNumberOfTuples() {
    if (!this.tupleVisitor.getAttributes().isEmpty()) {
      double avgNumberOfTuples = (double) totalNumberOfTuples() / this.tupleVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfTuples).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfTuples() {
    if (this.tupleVisitor.getAttributes().isEmpty()){ return 0; }
    int max = this.tupleVisitor.visit(this.tupleVisitor.getAttributes().get(0)).size();
    for (AttributeTreeImpl attribute: this.tupleVisitor.getAttributes()) {
      int value = this.tupleVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

}

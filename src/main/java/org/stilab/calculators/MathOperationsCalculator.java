package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.MathOperationsVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathOperationsCalculator {

  MathOperationsVisitor mathOperationsVisitor;

  public MathOperationsCalculator(BlockTreeImpl identifiedBlock){
    mathOperationsVisitor = new MathOperationsVisitor();
    mathOperationsVisitor.filterMathOperationsFromBlock(identifiedBlock);
  }

  public int totalNumberOfMathOperation(){
    return mathOperationsVisitor.getMathOperators().size();
  }

  public double avgNumberOfMathOperation() {
    if (!mathOperationsVisitor.getAttributes().isEmpty()) {
      double avgNumberOfMathOperation = (double) totalNumberOfMathOperation() / mathOperationsVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfMathOperation).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfMathOperation(){
    if (mathOperationsVisitor.getAttributes().isEmpty()){ return 0;}

    int max = mathOperationsVisitor.visit(mathOperationsVisitor.getAttributes().get(0)).size();
    for (AttributeTreeImpl attribute: mathOperationsVisitor.getAttributes()){
      int value = mathOperationsVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;
  }


}

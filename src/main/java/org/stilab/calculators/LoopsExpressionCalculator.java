package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.LoopsExpressionVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoopsExpressionCalculator {

    LoopsExpressionVisitor loopsExpressionVisitor;

    public LoopsExpressionCalculator(BlockTreeImpl identifiedBlock){
      loopsExpressionVisitor = new LoopsExpressionVisitor();
      loopsExpressionVisitor.filterLoopsFromBlock(identifiedBlock);
    }

    public int totalNumberOfLoops() {
      return loopsExpressionVisitor.getLoops().size();
    }

    public double avgNumberOfLoops(){
      if (!loopsExpressionVisitor.getAttributes().isEmpty()) {
        double avgNumberOfLoops = (double) totalNumberOfLoops() / loopsExpressionVisitor.getAttributes().size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfLoops).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfLoops(){
      if (loopsExpressionVisitor.getAttributes().isEmpty()){ return 0; }

      int max = loopsExpressionVisitor.visit(loopsExpressionVisitor.getAttributes().get(0)).size();
      for(AttributeTreeImpl attribute: loopsExpressionVisitor.getAttributes()) {
        int tmpValue = loopsExpressionVisitor.visit(attribute).size();
        if (tmpValue > max) {
          max = tmpValue;
        }
      }
      return max;
    }

}

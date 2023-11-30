package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ComparisonOperatorsVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComparisonOperatorsCalculator {

      private ComparisonOperatorsVisitor comparisonOperatorsVisitor;

      public ComparisonOperatorsCalculator(BlockTreeImpl identifiedBlock){
        comparisonOperatorsVisitor = new ComparisonOperatorsVisitor();
        comparisonOperatorsVisitor.filterComparisonOperatorsFromBlock(identifiedBlock);
      }

      public int totalNumberOfComparisonOperation(){
        return comparisonOperatorsVisitor.getComparisonOperations().size();
      }

      public double avgNumberOfComparisonOperation(){
        if (!comparisonOperatorsVisitor.getAttributes().isEmpty()){
          double avgNumberOfComparisonOperation = (double) totalNumberOfComparisonOperation() / comparisonOperatorsVisitor.getAttributes().size();
          BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfComparisonOperation).setScale(2, RoundingMode.HALF_UP);
          return roundedAverage.doubleValue();
        }
        return 0.0;
      }

      public int maxNumberOfComparisonOperation(){
        if (comparisonOperatorsVisitor.getAttributes().isEmpty()){ return 0; }
        int max = comparisonOperatorsVisitor.visit(comparisonOperatorsVisitor.getAttributes().get(0)).size();
        for(AttributeTreeImpl attribute: comparisonOperatorsVisitor.getAttributes()) {
          int value = comparisonOperatorsVisitor.visit(attribute).size();
          if (value > max) {
            max = value;
          }
        }
        return max;
      }

}

package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ConditionalExpressionVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConditionalExpressionCalculator {

    private ConditionalExpressionVisitor conditionalExpressionVisitor;

    public ConditionalExpressionCalculator(BlockTreeImpl identifiedBlock){
      conditionalExpressionVisitor = new ConditionalExpressionVisitor();
      conditionalExpressionVisitor.filtersConditionsFromBlock(identifiedBlock);
    }

    public int totalNumberOfConditions(){
      return conditionalExpressionVisitor.getConditions().size();
    }

    public double avgNumberOfConditionsPerAttribute(){
      if (!conditionalExpressionVisitor.getAttributes().isEmpty()){
        double avgNumberOfConditionsPerAttribute = (double) conditionalExpressionVisitor.getConditions().size()  / conditionalExpressionVisitor.getAttributes().size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfConditionsPerAttribute).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfConditionsPerAttribute(){

      if (conditionalExpressionVisitor.getAttributes().isEmpty()){ return 0; }

      int max = conditionalExpressionVisitor.visit(conditionalExpressionVisitor.getAttributes().get(0)).size();
      for (AttributeTreeImpl attributeTree: conditionalExpressionVisitor.getAttributes()){
        int tmpValue = conditionalExpressionVisitor.visit(attributeTree).size();
        if (max < tmpValue ){
          max = tmpValue;
        }
      }
      return max;
    }

}

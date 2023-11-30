package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.FunctionCallExpressionVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FunctionCallExpressionCalculator {

      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();

      public FunctionCallExpressionCalculator(BlockTreeImpl identifiedBlock){
        functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
        functionCallExpressionVisitor.filterFCfromBlock(identifiedBlock);
      }

    //    number of function call
    public int totalNumberOfFunctionCall() {
      return functionCallExpressionVisitor.getFunctionsCallPerBlock().size();
    }

    public double avgNumberOfFunctionCall(){

      if (!functionCallExpressionVisitor.getAttributes().isEmpty()) {
        double avgNumberOfFunctionCall = (double) functionCallExpressionVisitor.getFunctionsCallPerBlock().size() / functionCallExpressionVisitor.getAttributes().size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfFunctionCall).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }

      return 0.0;
    }

    public int maxNumberOfFunctionCall(){
      if (functionCallExpressionVisitor.getAttributes().isEmpty()){ return 0; }

      int max = functionCallExpressionVisitor.visit(functionCallExpressionVisitor.getAttributes().get(0)).size();
      for(AttributeTreeImpl attribute: functionCallExpressionVisitor.getAttributes()) {
        int value = functionCallExpressionVisitor.visit(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }


}

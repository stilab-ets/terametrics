package org.stilab.calculators;


import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;
import org.stilab.visitors.FunctionParametersVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FunctionParametersCalculator  {

  FunctionParametersVisitor functionParametersVisitor ;
  public FunctionParametersCalculator(BlockTreeImpl identifiedBlock) {
    functionParametersVisitor = new FunctionParametersVisitor();
    functionParametersVisitor.identifyUsedParametersInBlock(identifiedBlock);
  }

  //  Total Number Of Parameters Per Block
  public int totalNumberParamsPerBlock() {
    return functionParametersVisitor.getParameters().size();
  }

  //  Avg Number Of Parameters Per Block
  public double avgNumberParamsPerBlock() {
    if (!functionParametersVisitor.getFunctionCallTrees().isEmpty()) {
      double avgNumberParamsPerBlock = (double) totalNumberParamsPerBlock() / functionParametersVisitor.getFunctionCallTrees().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberParamsPerBlock).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  //  Max Number Of Parameters Per Block
  public int maxNumberParamsPerBlock() {

    if (functionParametersVisitor.getFunctionCallTrees().isEmpty()){ return 0; }
    int max = functionParametersVisitor.getFunctionCallTrees().get(0).arguments().trees().size();
    for (FunctionCallTreeImpl functionCallTree: functionParametersVisitor.getFunctionCallTrees()) {
      int numParamsPerFunction = functionCallTree.arguments().trees().size();
      if (max < numParamsPerFunction) {
        max = numParamsPerFunction;
      }
    }

    return max;
  }

}

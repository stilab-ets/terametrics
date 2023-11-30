package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.FunctionCallExpressionVisitor;
import org.stilab.visitors.LookUpFunctionVisitor;

public class LookUpFunctionCalculator {

  private LookUpFunctionVisitor lookUpFunctionVisitor;

  public LookUpFunctionCalculator(BlockTreeImpl identifiedBlock){
    FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    lookUpFunctionVisitor = new LookUpFunctionVisitor(functionCallExpressionVisitor);
    lookUpFunctionVisitor.identifyLookUpFunction(identifiedBlock);
  }

  public int countNumberOfLookUpFunction() {
    return lookUpFunctionVisitor.getLookups().size();
  }

}

package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DeprecatedFunctionsVisitor;
import org.stilab.visitors.FunctionCallExpressionVisitor;

public class DeprecatedFunctionsCalculator {

  private DeprecatedFunctionsVisitor deprecatedFunctionsVisitor;

  public DeprecatedFunctionsCalculator(BlockTreeImpl identifiedBlock){
    FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    deprecatedFunctionsVisitor = new DeprecatedFunctionsVisitor(functionCallExpressionVisitor);
    deprecatedFunctionsVisitor.identifyDeprecatedFunctions(identifiedBlock);
  }

  public int countDeprecatedFunctions(){
    return deprecatedFunctionsVisitor.getDeprecatedFunctions().size();
  }


}

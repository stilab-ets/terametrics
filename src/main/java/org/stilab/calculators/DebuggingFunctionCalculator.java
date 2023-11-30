package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DebuggingFunctionVisitor;
import org.stilab.visitors.FunctionCallExpressionVisitor;

public class DebuggingFunctionCalculator {

    private DebuggingFunctionVisitor debuggingFunctionVisitor;
    public DebuggingFunctionCalculator(BlockTreeImpl identifiedBlock){
      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
      debuggingFunctionVisitor = new DebuggingFunctionVisitor(functionCallExpressionVisitor);
      debuggingFunctionVisitor.identifyDebuggingFunction(identifiedBlock);
    }

    public int countDebuggingFunctions(){
      return debuggingFunctionVisitor.getDebuggingFunctions().size();
    }


}

package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class DebuggingFunctionVisitor{

  private FunctionCallExpressionVisitor functionCallExpressionVisitor;

  private List<FunctionCallTreeImpl> debuggingFunctions = new ArrayList<>();

  public DebuggingFunctionVisitor(FunctionCallExpressionVisitor
                                             functionCallExpressionVisitor
    ) {
      this.functionCallExpressionVisitor = functionCallExpressionVisitor;
  }

  public void identifyDebuggingFunction(BlockTreeImpl block) {
    List<FunctionCallTreeImpl> functionsCalls =
      this.functionCallExpressionVisitor.filterFCfromBlock(block);

    for (FunctionCallTreeImpl funCall: functionsCalls) {
      if (
           funCall.name().value().equals("try") ||
           funCall.name().value().equals("can") ||
           funCall.name().value().equals("type")
      ) {
        debuggingFunctions.add(funCall);
      }
    }
  }

  public List<FunctionCallTreeImpl> getDebuggingFunctions() {
    return debuggingFunctions;
  }


}

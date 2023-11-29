package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DebuggingFunctionVisitor;
import org.stilab.visitors.FunctionCallExpressionVisitor;

public class DebuggingFunctionCollector implements Decorator {


  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    DebuggingFunctionVisitor debuggingFunctionVisitor = new DebuggingFunctionVisitor(functionCallExpressionVisitor);
    debuggingFunctionVisitor.identifyDebuggingFunction(identifiedBlock);
    int numDebuggingFunctions = debuggingFunctionVisitor.getDebuggingFunctions().size();
    metrics.put("numDebuggingFunctions", numDebuggingFunctions);
    return metrics;
  }

}

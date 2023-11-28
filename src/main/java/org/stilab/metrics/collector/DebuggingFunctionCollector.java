package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.DebuggingFunctionVisitor;
import org.stilab.metrics.visitors.FunctionCallExpressionVisitor;

public class DebuggingFunctionCollector implements Repository {


  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    DebuggingFunctionVisitor debuggingFunctionVisitor = new DebuggingFunctionVisitor(functionCallExpressionVisitor);
    debuggingFunctionVisitor.identifyDebuggingFunction(identifiedBlock);
    int numDebuggingFunctions = debuggingFunctionVisitor.getDebuggingFunctions().size();
    metrics.put("numDebuggingFunctions", numDebuggingFunctions);
    return metrics;
  }

}

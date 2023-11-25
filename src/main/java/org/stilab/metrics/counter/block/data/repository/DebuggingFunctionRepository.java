package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.DebuggingFunctionVisitor;
import org.stilab.metrics.counter.block.visitors.FunctionCallExpressionVisitor;

public class DebuggingFunctionRepository implements Repository {


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

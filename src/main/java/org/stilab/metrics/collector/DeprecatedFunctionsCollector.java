package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.DeprecatedFunctionsVisitor;
import org.stilab.metrics.visitors.FunctionCallExpressionVisitor;

public class DeprecatedFunctionsCollector implements Repository {


  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    DeprecatedFunctionsVisitor deprecatedFunctionsVisitor = new DeprecatedFunctionsVisitor(functionCallExpressionVisitor);
    deprecatedFunctionsVisitor.identifyDeprecatedFunctions(identifiedBlock);
    int numDeprecatedFunctions = deprecatedFunctionsVisitor.getDeprecatedFunctions().size();
    metrics.put("numDeprecatedFunctions", numDeprecatedFunctions);
    return metrics;

  }

}

package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DeprecatedFunctionsVisitor;
import org.stilab.visitors.FunctionCallExpressionVisitor;

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

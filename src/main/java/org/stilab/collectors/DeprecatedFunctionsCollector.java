package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.DeprecatedFunctionsCalculator;
import org.stilab.visitors.DeprecatedFunctionsVisitor;
import org.stilab.visitors.FunctionCallExpressionVisitor;

public class DeprecatedFunctionsCollector implements Decorator {


  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

//    int numDeprecatedFunctions = deprecatedFunctionsVisitor.getDeprecatedFunctions().size();
    DeprecatedFunctionsCalculator deprecatedFunctionsCalculator = new DeprecatedFunctionsCalculator(identifiedBlock);
    metrics.put("numDeprecatedFunctions", deprecatedFunctionsCalculator.countDeprecatedFunctions());
    return metrics;

  }

}

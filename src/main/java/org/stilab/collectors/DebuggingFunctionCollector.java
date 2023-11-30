package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.DebuggingFunctionCalculator;

public class DebuggingFunctionCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    DebuggingFunctionCalculator debuggingFunctionCalculator = new DebuggingFunctionCalculator(identifiedBlock);
    metrics.put("numDebuggingFunctions", debuggingFunctionCalculator.countDebuggingFunctions());
    return metrics;
  }

}

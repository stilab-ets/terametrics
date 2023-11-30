package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.LogicalOperationsCalculator;
import org.stilab.visitors.LogicalOperationsVisitor;

public class LogicalOperationsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    LogicalOperationsCalculator logicalOperationsCalculator = new LogicalOperationsCalculator(identifiedBlock);
    int numLogiOpers = logicalOperationsCalculator.totalNumberOfLogicalOperations();
    double avgLogiOpers = logicalOperationsCalculator.avgNumberOfLogicalOperations();
    int maxLogiOpers = logicalOperationsCalculator.maxNumberOfLogicalOperations();

    metrics.put("numLogiOpers", numLogiOpers);
    metrics.put("avgLogiOpers", avgLogiOpers);
    metrics.put("maxLogiOpers", maxLogiOpers);

    return metrics;
  }

}

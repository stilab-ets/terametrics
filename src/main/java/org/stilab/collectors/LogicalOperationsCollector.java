package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.LogicalOperationsVisitor;

public class LogicalOperationsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    LogicalOperationsVisitor logicalOperationsVisitor = new LogicalOperationsVisitor();

    logicalOperationsVisitor.filterLogicalOperationsFromBlock(identifiedBlock);
    int numLogiOpers = logicalOperationsVisitor.totalNumberOfLogicalOperations();
    double avgLogiOpers = logicalOperationsVisitor.avgNumberOfLogicalOperations();
    int maxLogiOpers = logicalOperationsVisitor.maxNumberOfLogicalOperations();

    metrics.put("numLogiOpers", numLogiOpers);
    metrics.put("avgLogiOpers", avgLogiOpers);
    metrics.put("maxLogiOpers", maxLogiOpers);

    return metrics;
  }

}

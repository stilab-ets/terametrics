package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.visitors.LogicalOperationsVisitor;

public class LogicalOperationsRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

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

package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.visitors.MathOperationsVisitor;

public class MathOperationsRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    MathOperationsVisitor mathOperationsVisitor = new MathOperationsVisitor();

    mathOperationsVisitor.filterMathOperationsFromBlock(identifiedBlock);
    int numMathOperations = mathOperationsVisitor.totalNumberOfMathOperation();
    double avgMathOperations = mathOperationsVisitor.avgNumberOfMathOperation();
    int maxMathOperations = mathOperationsVisitor.maxNumberOfMathOperation();

    metrics.put("numMathOperations", numMathOperations);
    metrics.put("avgMathOperations", avgMathOperations);
    metrics.put("maxMathOperations", maxMathOperations);

    return metrics;

  }

}

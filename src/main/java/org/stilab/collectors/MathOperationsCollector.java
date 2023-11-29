package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.MathOperationsVisitor;

public class MathOperationsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

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

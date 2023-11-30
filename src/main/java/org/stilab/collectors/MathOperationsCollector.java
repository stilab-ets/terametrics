package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.MathOperationsCalculator;
import org.stilab.visitors.MathOperationsVisitor;

public class MathOperationsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

//    MathOperationsVisitor mathOperationsVisitor = new MathOperationsVisitor();
//
//    mathOperationsVisitor.filterMathOperationsFromBlock(identifiedBlock);

    MathOperationsCalculator mathOperationsCalculator = new MathOperationsCalculator(identifiedBlock);

    int numMathOperations = mathOperationsCalculator.totalNumberOfMathOperation();
    double avgMathOperations = mathOperationsCalculator.avgNumberOfMathOperation();
    int maxMathOperations = mathOperationsCalculator.maxNumberOfMathOperation();

    metrics.put("numMathOperations", numMathOperations);
    metrics.put("avgMathOperations", avgMathOperations);
    metrics.put("maxMathOperations", maxMathOperations);

    return metrics;

  }

}

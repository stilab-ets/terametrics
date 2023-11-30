package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.ComparisonOperatorsCalculator;
import org.stilab.visitors.ComparisonOperatorsVisitor;

public class ComparisonOperatorsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      ComparisonOperatorsCalculator comparisonOperatorsCalculator = new ComparisonOperatorsCalculator(identifiedBlock);

      int numComparisonOperators = comparisonOperatorsCalculator.totalNumberOfComparisonOperation();
      double avgComparisonOperators = comparisonOperatorsCalculator.avgNumberOfComparisonOperation();
      int maxComparisonOperators = comparisonOperatorsCalculator.maxNumberOfComparisonOperation();

      metrics.put("numComparisonOperators", numComparisonOperators);
      metrics.put("avgComparisonOperators", avgComparisonOperators);
      metrics.put("maxComparisonOperators", maxComparisonOperators);

      return metrics;
    }

}

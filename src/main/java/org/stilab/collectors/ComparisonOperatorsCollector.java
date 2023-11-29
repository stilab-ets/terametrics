package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ComparisonOperatorsVisitor;

public class ComparisonOperatorsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      ComparisonOperatorsVisitor comparisonOperatorsVisitor = new ComparisonOperatorsVisitor();

      comparisonOperatorsVisitor.filterComparisonOperatorsFromBlock(identifiedBlock);

      int numComparisonOperators = comparisonOperatorsVisitor.totalNumberOfComparisonOperation();
      double avgComparisonOperators = comparisonOperatorsVisitor.avgNumberOfComparisonOperation();
      int maxComparisonOperators = comparisonOperatorsVisitor.maxNumberOfComparisonOperation();

      metrics.put("numComparisonOperators", numComparisonOperators);
      metrics.put("avgComparisonOperators", avgComparisonOperators);
      metrics.put("maxComparisonOperators", maxComparisonOperators);

      return metrics;
    }

}

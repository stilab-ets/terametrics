package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.ComparisonOperatorsVisitor;

public class ComparisonOperatorsRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

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

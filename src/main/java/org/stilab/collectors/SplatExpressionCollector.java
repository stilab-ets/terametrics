package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.SplatExpressionCalculator;
import org.stilab.visitors.SplatExpressionVisitor;

public class SplatExpressionCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      SplatExpressionCalculator splatExpressionCalculator = new SplatExpressionCalculator(identifiedBlock);

      int numSplatExpressions = splatExpressionCalculator.totalSplatExpressions();
      double avgSplatExpressions = splatExpressionCalculator.avgSplatExpressions();
      int maxSplatExpressions = splatExpressionCalculator.maxSplatExpressions();

      metrics.put("numSplatExpressions", numSplatExpressions);
      metrics.put("avgSplatExpressions", avgSplatExpressions);
      metrics.put("maxSplatExpressions", maxSplatExpressions);

      return metrics;
    }
}

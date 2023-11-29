package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.SplatExpressionVisitor;

public class SplatExpressionCollector implements Repository {


    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      SplatExpressionVisitor splatExpressionVisitor = new SplatExpressionVisitor();

      splatExpressionVisitor.filtersSplatsFromBlock(identifiedBlock);
      int numSplatExpressions = splatExpressionVisitor.totalSplatExpressions();
      double avgSplatExpressions = splatExpressionVisitor.avgSplatExpressions();
      int maxSplatExpressions = splatExpressionVisitor.maxSplatExpressions();

      metrics.put("numSplatExpressions", numSplatExpressions);
      metrics.put("avgSplatExpressions", avgSplatExpressions);
      metrics.put("maxSplatExpressions", maxSplatExpressions);

      return metrics;
    }
}

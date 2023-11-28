package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.visitors.ConditionalExpressionVisitor;

public class ConditionalExpressionRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ConditionalExpressionVisitor conditionalExpressionVisitor = new ConditionalExpressionVisitor();
      conditionalExpressionVisitor.filtersConditionsFromBlock(identifiedBlock);
      int numConditions = conditionalExpressionVisitor.totalNumberOfConditions();
      int maxConditionsPerAttr = conditionalExpressionVisitor.maxNumberOfConditionsPerAttribute();
      double avgConditionsPerAttr = conditionalExpressionVisitor.avgNumberOfConditionsPerAttribute();

      metrics.put("numConditions", numConditions);
      metrics.put("avgConditions", avgConditionsPerAttr);
      metrics.put("maxConditions",maxConditionsPerAttr);

      return metrics;
    }
}

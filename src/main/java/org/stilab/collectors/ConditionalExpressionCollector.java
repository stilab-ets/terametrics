package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.ConditionalExpressionCalculator;

public class ConditionalExpressionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ConditionalExpressionCalculator conditionalExpressionCalculator = new ConditionalExpressionCalculator(identifiedBlock);

      int numConditions = conditionalExpressionCalculator.totalNumberOfConditions();
      int maxConditionsPerAttr = conditionalExpressionCalculator.maxNumberOfConditionsPerAttribute();
      double avgConditionsPerAttr = conditionalExpressionCalculator.avgNumberOfConditionsPerAttribute();

      metrics.put("numConditions", numConditions);
      metrics.put("avgConditions", avgConditionsPerAttr);
      metrics.put("maxConditions",maxConditionsPerAttr);

      return metrics;
    }
}

package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.TemplateExpressionCalculator;
import org.stilab.visitors.TemplateExpressionVisitor;

public class TemplateExpressionCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TemplateExpressionCalculator templateExpressionCalculator = new TemplateExpressionCalculator(identifiedBlock);
      int numTemplateExpression = templateExpressionCalculator.totalNumberOfTemplateExpressionsPerBlock();
      double avgNumTemplateExpression = templateExpressionCalculator.avgNumOfTemplateExpressionPerBlock();

      metrics.put("numTemplateExpression", numTemplateExpression);
      metrics.put("avgTemplateExpression", avgNumTemplateExpression);

      return metrics;
    }

}

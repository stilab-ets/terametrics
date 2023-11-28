package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.TemplateExpressionVisitor;

public class TemplateExpressionCollector implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TemplateExpressionVisitor templateExpressionVisitor = new TemplateExpressionVisitor();

      templateExpressionVisitor.filterTemplateExpressionsFromBlock(identifiedBlock);
      int numTemplateExpression = templateExpressionVisitor.totalNumberOfTemplateExpressionsPerBlock();
      double avgNumTemplateExpression = templateExpressionVisitor.avgNumOfTemplateExpressionPerBlock();

      metrics.put("numTemplateExpression", numTemplateExpression);
      metrics.put("avgTemplateExpression", avgNumTemplateExpression);

      return metrics;
    }

}

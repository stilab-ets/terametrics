package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.LiteralExpressionCalculator;
import org.stilab.visitors.LiteralExpressionVisitor;

public class LiteralExpressionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      LiteralExpressionCalculator literalExpressionCalculator = new LiteralExpressionCalculator(identifiedBlock);

      int numLiteralExpressions = literalExpressionCalculator.totalNumberOfLiteralExpressions();
      int numStringValues = literalExpressionCalculator.numStringValues();
//      int sumLengthStringValues = literalExpressionVisitor.sumLengthOfStringLiteralExpr();
//      double avgLengthStringValues = literalExpressionVisitor.avgLengthOfStringLiteralExpr();
//      int maxLengthStringValues = literalExpressionVisitor.maxLengthOfStringLiteralExpr();
  //
      metrics.put("numLiteralExpression", numLiteralExpressions);
      metrics.put("numStringValues", numStringValues);
//      metrics.put("sumLengthStringValues", sumLengthStringValues);
//      metrics.put("avgLengthStringValues", avgLengthStringValues);
//      metrics.put("maxLengthStringValues", maxLengthStringValues);

      return metrics;
    }
}

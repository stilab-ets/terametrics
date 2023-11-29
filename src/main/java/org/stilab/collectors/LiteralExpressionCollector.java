package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.LiteralExpressionVisitor;

public class LiteralExpressionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();

      literalExpressionVisitor.filterLiteralExprFromBlock(identifiedBlock);

      int numLiteralExpressions = literalExpressionVisitor.totalNumberOfLiteralExpressions();
      int numStringValues = literalExpressionVisitor.numStringValues();
      int sumLengthStringValues = literalExpressionVisitor.sumLengthOfStringLiteralExpr();
      double avgLengthStringValues = literalExpressionVisitor.avgLengthOfStringLiteralExpr();
      int maxLengthStringValues = literalExpressionVisitor.maxLengthOfStringLiteralExpr();
  //
      metrics.put("numLiteralExpression", numLiteralExpressions);
      metrics.put("numStringValues", numStringValues);
      metrics.put("sumLengthStringValues", sumLengthStringValues);
      metrics.put("avgLengthStringValues", avgLengthStringValues);
      metrics.put("maxLengthStringValues", maxLengthStringValues);

      return metrics;
    }
}

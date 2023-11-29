package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.FunctionCallExpressionVisitor;
import org.stilab.visitors.LookUpFunctionVisitor;

public class LookUpFunctionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();

      LookUpFunctionVisitor lookUpFunctionVisitor = new LookUpFunctionVisitor(functionCallExpressionVisitor);

      lookUpFunctionVisitor.identifyLookUpFunction(identifiedBlock);
      int numLookUpFunctionCall = lookUpFunctionVisitor.getLookups().size();
      metrics.put("numLookUpFunctionCall", numLookUpFunctionCall);
      return metrics;
    }

}

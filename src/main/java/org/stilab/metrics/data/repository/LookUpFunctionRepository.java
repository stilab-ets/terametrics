package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.FunctionCallExpressionVisitor;
import org.stilab.metrics.visitors.LookUpFunctionVisitor;

public class LookUpFunctionRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();

      LookUpFunctionVisitor lookUpFunctionVisitor = new LookUpFunctionVisitor(functionCallExpressionVisitor);

      lookUpFunctionVisitor.identifyLookUpFunction(identifiedBlock);
      int numLookUpFunctionCall = lookUpFunctionVisitor.getLookups().size();
      metrics.put("numLookUpFunctionCall", numLookUpFunctionCall);
      return metrics;
    }

}

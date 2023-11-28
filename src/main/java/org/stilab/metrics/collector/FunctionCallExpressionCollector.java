package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.FunctionCallExpressionVisitor;

public class FunctionCallExpressionCollector implements Repository {


    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();

      functionCallExpressionVisitor.filterFCfromBlock(identifiedBlock);
      int numFunctionCall = functionCallExpressionVisitor.totalNumberOfFunctionCall();
      double avgFunctionCall = functionCallExpressionVisitor.avgNumberOfFunctionCall();
      int maxFunctionCall = functionCallExpressionVisitor.maxNumberOfFunctionCall();

      metrics.put("numFunctionCall", numFunctionCall);
      metrics.put("avgFunctionCall", avgFunctionCall);
      metrics.put("maxFunctionCall", maxFunctionCall);
      return metrics;
    }

}

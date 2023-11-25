package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.FunctionCallExpressionVisitor;

public class FunctionCallExpressionRepository implements Repository {


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

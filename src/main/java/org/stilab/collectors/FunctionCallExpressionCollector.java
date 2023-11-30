package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.FunctionCallExpressionCalculator;
import org.stilab.visitors.FunctionCallExpressionVisitor;

public class FunctionCallExpressionCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      FunctionCallExpressionCalculator functionCallExpressionCalculator = new FunctionCallExpressionCalculator(identifiedBlock);

      int numFunctionCall = functionCallExpressionCalculator.totalNumberOfFunctionCall();
      double avgFunctionCall = functionCallExpressionCalculator.avgNumberOfFunctionCall();
      int maxFunctionCall = functionCallExpressionCalculator.maxNumberOfFunctionCall();

      metrics.put("numFunctionCall", numFunctionCall);
      metrics.put("avgFunctionCall", avgFunctionCall);
      metrics.put("maxFunctionCall", maxFunctionCall);
      return metrics;
    }

}

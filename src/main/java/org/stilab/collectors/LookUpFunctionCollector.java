package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.LookUpFunctionCalculator;
import org.stilab.visitors.FunctionCallExpressionVisitor;
import org.stilab.visitors.LookUpFunctionVisitor;

public class LookUpFunctionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      LookUpFunctionCalculator lookUpFunctionCalculator = new LookUpFunctionCalculator(identifiedBlock);

      int numLookUpFunctionCall = lookUpFunctionCalculator.countNumberOfLookUpFunction();
      metrics.put("numLookUpFunctionCall", numLookUpFunctionCall);
      return metrics;
    }

}

package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.FunctionParametersCalculator;
import org.stilab.visitors.FunctionParametersVisitor;

public class FunctionParametersCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      FunctionParametersCalculator functionParametersCalculator = new FunctionParametersCalculator(identifiedBlock);

      int numParams = functionParametersCalculator.totalNumberParamsPerBlock();
      double avgParams = functionParametersCalculator.avgNumberParamsPerBlock();
      int maxParams = functionParametersCalculator.maxNumberParamsPerBlock();

      metrics.put("numParams", numParams);
      metrics.put("avgParams", avgParams);
      metrics.put("maxParams", maxParams);

      return metrics;
    }
}

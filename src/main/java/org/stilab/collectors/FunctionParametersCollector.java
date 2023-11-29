package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.FunctionParametersVisitor;

public class FunctionParametersCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      FunctionParametersVisitor functionParametersVisitor = new FunctionParametersVisitor();

      functionParametersVisitor.identifyUsedParametersInBlock(identifiedBlock);
      int numParams = functionParametersVisitor.totalNumberParamsPerBlock();
      double avgParams = functionParametersVisitor.avgNumberParamsPerBlock();
      int maxParams = functionParametersVisitor.maxNumberParamsPerBlock();

      metrics.put("numParams", numParams);
      metrics.put("avgParams", avgParams);
      metrics.put("maxParams", maxParams);

      return metrics;
    }
}

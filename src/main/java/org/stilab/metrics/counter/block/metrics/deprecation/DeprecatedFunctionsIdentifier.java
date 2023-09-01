package org.stilab.metrics.counter.block.metrics.deprecation;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;
import org.stilab.metrics.counter.block.metrics.FunctionCallExpressionIdentifier;

import java.util.ArrayList;
import java.util.List;

public class DeprecatedFunctionsIdentifier {

  private FunctionCallExpressionIdentifier functionCallExpressionIdentifier;

  private List<FunctionCallTreeImpl> deprecatedFunctions = new ArrayList<>();

  public DeprecatedFunctionsIdentifier(FunctionCallExpressionIdentifier
                                         functionCallExpressionIdentifier) {
    this.functionCallExpressionIdentifier = functionCallExpressionIdentifier;
  }

  public void identifyDeprecatedFunctions(BlockTreeImpl block){
    List<FunctionCallTreeImpl> functionsCalls =
      this.functionCallExpressionIdentifier.filterFCfromBlock(block);

    for (FunctionCallTreeImpl funCall: functionsCalls) {
      if (funCall.name().value().equals("map") || funCall.name().value().equals("list") ) {
        deprecatedFunctions.add(funCall);
      }
    }
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {
    this.identifyDeprecatedFunctions(identifiedBlock);
    int numDeprecatedFunctions = this.deprecatedFunctions.size();
    metrics.put("numDeprecatedFunctions", numDeprecatedFunctions);
    return metrics;
  }

}

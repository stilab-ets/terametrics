package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class DebuggingFunctionIdentifier {

  private FunctionCallExpressionIdentifier functionCallExpressionIdentifier;

  private List<FunctionCallTreeImpl> debuggingFunctions = new ArrayList<>();

  public DebuggingFunctionIdentifier(FunctionCallExpressionIdentifier
        functionCallExpressionIdentifier
    ) {
      this.functionCallExpressionIdentifier = functionCallExpressionIdentifier;
  }

  public void identifyDebuggingFunction(BlockTreeImpl block) {
    List<FunctionCallTreeImpl> functionsCalls =
      this.functionCallExpressionIdentifier.filterFCfromBlock(block);

    for (FunctionCallTreeImpl funCall: functionsCalls) {
      if (
           funCall.name().value().equals("try") ||
           funCall.name().value().equals("can") ||
           funCall.name().value().equals("type")
      ) {
        debuggingFunctions.add(funCall);
      }
    }
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {
    this.identifyDebuggingFunction(identifiedBlock);
    int numDebuggingFunctions = this.debuggingFunctions.size();
    metrics.put("numDebuggingFunctions", numDebuggingFunctions);
    return metrics;
  }

}

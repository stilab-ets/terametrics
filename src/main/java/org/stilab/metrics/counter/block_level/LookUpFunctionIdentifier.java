package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class LookUpFunctionIdentifier {

    private FunctionCallExpressionIdentifier functionCallExpressionIdentifier;

    private List<FunctionCallTreeImpl> lookups = new ArrayList<>();

    public LookUpFunctionIdentifier(FunctionCallExpressionIdentifier
                                      functionCallExpressionIdentifier
                                   ) {
           this.functionCallExpressionIdentifier = functionCallExpressionIdentifier;
    }

    public void identifyLookUpFunction(BlockTreeImpl block) {

      List<FunctionCallTreeImpl> functionsCalls =
        this.functionCallExpressionIdentifier.filterFCfromBlock(block);

      for (FunctionCallTreeImpl funCall: functionsCalls) {
         if (funCall.name().value().equals("lookup")) {
           lookups.add(funCall);
         }
      }
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {
      this.identifyLookUpFunction(identifiedBlock);
      int numLookUpFunctionCall = this.lookups.size();
      metrics.put("numLookUpFunctionCall", numLookUpFunctionCall);
      return metrics;
    }

}

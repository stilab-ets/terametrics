package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class DeprecatedFunctionsVisitor {

  private FunctionCallExpressionVisitor functionCallExpressionVisitor;

  private List<FunctionCallTreeImpl> deprecatedFunctions = new ArrayList<>();

  public DeprecatedFunctionsVisitor(FunctionCallExpressionVisitor
                                               functionCallExpressionVisitor) {
    this.functionCallExpressionVisitor = functionCallExpressionVisitor;
  }

  public void identifyDeprecatedFunctions(BlockTreeImpl block){
    List<FunctionCallTreeImpl> functionsCalls =
      this.functionCallExpressionVisitor.filterFCfromBlock(block);

    for (FunctionCallTreeImpl funCall: functionsCalls) {
      if (funCall.name().value().equals("map") || funCall.name().value().equals("list") ) {
        deprecatedFunctions.add(funCall);
      }
    }
  }

  public List<FunctionCallTreeImpl> getDeprecatedFunctions() {
    return deprecatedFunctions;
  }

//  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {
//    this.identifyDeprecatedFunctions(identifiedBlock);
//    int numDeprecatedFunctions = this.deprecatedFunctions.size();
//    metrics.put("numDeprecatedFunctions", numDeprecatedFunctions);
//    return metrics;
//  }

}

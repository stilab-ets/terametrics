package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;
import org.stilab.metrics.counter.block.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class LookUpFunctionVisitor {

    private FunctionCallExpressionVisitor functionCallExpressionVisitor;

    private List<FunctionCallTreeImpl> lookups = new ArrayList<>();

    public LookUpFunctionVisitor(FunctionCallExpressionVisitor
                                   functionCallExpressionVisitor
                                   ){
           this.functionCallExpressionVisitor = functionCallExpressionVisitor;
    }

    public void identifyLookUpFunction(BlockTreeImpl block) {

      List<FunctionCallTreeImpl> functionsCalls =
        this.functionCallExpressionVisitor.filterFCfromBlock(block);

      for (FunctionCallTreeImpl funCall: functionsCalls) {
         if (funCall.name().value().equals("lookup")) {
           lookups.add(funCall);
         }
      }
    }

  public List<FunctionCallTreeImpl> getLookups() {
    return lookups;
  }


}

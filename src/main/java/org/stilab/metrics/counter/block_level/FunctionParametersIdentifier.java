package org.stilab.metrics.counter.block_level;

import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class FunctionParametersIdentifier {

  List<ExpressionTree> parameters = new ArrayList<>();
  List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();

  FunctionCallExpressionIdentifier functionCallExpressionIdentifier;
  public FunctionParametersIdentifier() {
    functionCallExpressionIdentifier = new FunctionCallExpressionIdentifier();
  }

  public List<ExpressionTree> identifyFunctionParameters(FunctionCallTreeImpl functionCallTree)  {
    return functionCallTree.arguments().trees();
  }

  public List<ExpressionTree> identifyUsedParametersInBlock(BlockTreeImpl blockTree) {
    functionCallTrees = functionCallExpressionIdentifier.filterFCfromBlock( blockTree );
    for ( FunctionCallTreeImpl fc: functionCallTrees ) {
      parameters.addAll(this.identifyFunctionParameters(fc) );
    }
    return parameters;
  }

  //  Total Number Of Parameters Per Block
  public int totalNumberParamsPerBlock() {
    return this.parameters.size();
  }

  //  Avg Number Of Parameters Per Block
  public double avgNumberParamsPerBlock() {
    if (this.functionCallTrees.size() >= 1) {
      return (double) totalNumberParamsPerBlock() / this.functionCallTrees.size();
    }
    return 0.0;
  }

  //  Max Number Of Parameters Per Block
  public int maxNumberParamsPerBlock() {
    int max = 0;
    for (FunctionCallTreeImpl functionCallTree: this.functionCallTrees) {
      int numParamsPerFunction = functionCallTree.arguments().trees().size();
      if (max <= numParamsPerFunction) {
        max = numParamsPerFunction;
      }
    }
    return max;
  }



}

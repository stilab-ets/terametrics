package org.stilab.metrics.counter.expression;

import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class FunctionParametersIdentifier {


  List<ExpressionTree> parameters = new ArrayList<>();

  FunctionCallExpressionIdentifier functionCallExpressionIdentifier = new FunctionCallExpressionIdentifier();

  public FunctionParametersIdentifier() {}


  public List<ExpressionTree> identifyFunctionParameters(FunctionCallTreeImpl
                                                 functionCallTree)  {

    return functionCallTree.arguments().trees();
  }

  public List<ExpressionTree> identifyUsedParametersInBlock(BlockTreeImpl blockTree) {

    List<FunctionCallTreeImpl> functionCallTrees
      = functionCallExpressionIdentifier.filterFCfromBlock( blockTree );

    for ( FunctionCallTreeImpl fc: functionCallTrees ) {
      parameters.addAll(this.identifyFunctionParameters(fc) );
    }

    return parameters;
  }

  public int countParametersPerBlock() {
    return this.parameters.size();
  }


}

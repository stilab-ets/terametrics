package org.stilab.visitors;

import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class FunctionParametersVisitor {

    List<ExpressionTree> parameters = new ArrayList<>();
    List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();
    FunctionCallExpressionVisitor functionCallExpressionVisitor;

    public FunctionCallExpressionVisitor getFunctionCallExpressionVisitor() {
      return functionCallExpressionVisitor;
    }

    public List<ExpressionTree> getParameters() {
      return parameters;
    }

    public List<FunctionCallTreeImpl> getFunctionCallTrees() {
      return functionCallTrees;
    }

    public FunctionParametersVisitor() {
      functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    }

    public List<ExpressionTree> identifyFunctionParameters(FunctionCallTreeImpl functionCallTree)  {
      return functionCallTree.arguments().trees();
    }

    public List<ExpressionTree> identifyUsedParametersInBlock(BlockTreeImpl blockTree) {
      functionCallTrees = functionCallExpressionVisitor.filterFCfromBlock( blockTree );
      for ( FunctionCallTreeImpl fc: functionCallTrees ) {
        parameters.addAll(this.identifyFunctionParameters(fc) );
      }
      return parameters;
    }

}

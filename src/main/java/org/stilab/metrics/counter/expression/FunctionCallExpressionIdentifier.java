package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallExpressionIdentifier {

    public List<FunctionCallTreeImpl> functionsCallPerBlock = new ArrayList<>();

    public FunctionCallExpressionIdentifier()  {}

    public List<FunctionCallTreeImpl> filterFunctionCall(AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();

      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

      return trees.stream()
                  .filter(child -> child instanceof FunctionCallTreeImpl)
                  .map(child -> (FunctionCallTreeImpl) child)
                  .collect(Collectors.toList());
    }

    public List<FunctionCallTreeImpl> filterFCfromAttributesList(List<AttributeTreeImpl> attributeTrees) {

      List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();

      for (AttributeTreeImpl attributeTree: attributeTrees) {
        functionCallTrees.addAll( this.filterFunctionCall(attributeTree) );
      }

      return functionCallTrees;
    }

//    root blocks
    public List<FunctionCallTreeImpl> filterFCfromBlock(BlockTreeImpl blockTree) {

      List<AttributeTreeImpl> attributeTrees =  (new AttrFinderImpl())
        .getAllAttributes(blockTree);

      functionsCallPerBlock = this.filterFCfromAttributesList(attributeTrees);

      return functionsCallPerBlock;
    }

//    number of function call
    public int countFunctionCallsPerBlock() {
      return functionsCallPerBlock.size();
    }

}

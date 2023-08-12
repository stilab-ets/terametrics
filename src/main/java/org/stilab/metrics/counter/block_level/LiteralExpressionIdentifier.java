package org.stilab.metrics.counter.block_level;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LiteralExpressionIdentifier {

  public LiteralExpressionIdentifier() {}

  public List<LiteralExprTreeImpl> literalExprTrees = new ArrayList<>();

  public List<LiteralExprTreeImpl> filterLiteralExpr
    (AttributeTreeImpl attributeTree) {
    ExpressionTree expressionTree = attributeTree.value();
    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
    return trees.stream()
      .filter(child -> child instanceof LiteralExprTreeImpl)
      .map(child -> (LiteralExprTreeImpl) child )
      .collect(Collectors.toList());
  }

  public List<LiteralExprTreeImpl> filterLiteralExprFromAttributesList(List<AttributeTreeImpl>
                                                                         attributeTrees) {
    List<LiteralExprTreeImpl> attributeAccessTrees = new ArrayList<>();
    for(AttributeTreeImpl attributeAccess: attributeTrees) {
      attributeAccessTrees.addAll( this.filterLiteralExpr(attributeAccess) );
    }
    return attributeAccessTrees;
  }

  public List<LiteralExprTreeImpl> filterLiteralExprFromBlock(BlockTreeImpl blockTree) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAllAttributes(blockTree);
    this.literalExprTrees = this.filterLiteralExprFromAttributesList(attributeTrees);
    return this.literalExprTrees;
  }

  public int measureLengthOfLiteralExprValue(LiteralExprTreeImpl literalExprTree) {
    return literalExprTree.value().length();
  }

  public int sumLengthOfLiteralExprValue(List<LiteralExprTreeImpl> literalExprs) {
    int sum = 0;
    for (LiteralExprTreeImpl literalExprTree: literalExprs) {
      sum += measureLengthOfLiteralExprValue(literalExprTree);
    }
    return sum;
  }

  public double avgLengthOfLiteralExprValue(List<LiteralExprTreeImpl> literalExprs) {
    if (literalExprs.size() >= 1) {
      return (double) sumLengthOfLiteralExprValue(literalExprs) / literalExprs.size();
    }
    return 0.0;
  }

}

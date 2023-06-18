package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BinaryExpressionTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryExpressionIdentifier {

  List<BinaryExpressionTreeImpl> binaryExpressionTreeList = new ArrayList<>();

  public BinaryExpressionIdentifier() {}

  public List<BinaryExpressionTreeImpl> filterBinaryExpressions
    (AttributeTreeImpl attributeTree) {

    ExpressionTree expressionTree = attributeTree.value();

    List<Tree> trees = ExpressionAnalyzer.getInstance()
      .getAllNestedExpressions(expressionTree);

    return trees.stream()
      .filter(child -> child instanceof BinaryExpressionTreeImpl)
      .map( child -> (BinaryExpressionTreeImpl) child)
      .collect(Collectors.toList());
  }

  public List<BinaryExpressionTreeImpl>
    filterBinaryExpressionsFromAttributesList(
    List<AttributeTreeImpl> attributeTrees
  ) {
    List<BinaryExpressionTreeImpl> binaryOperations = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributeTrees) {
      binaryOperations.addAll(
        this.filterBinaryExpressions(attribute)
      );
    }

    return binaryOperations;
  }

  public List<BinaryExpressionTreeImpl> filtersBinaryExpressionsFromBlock(
    BlockTreeImpl blockTree
  ) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.binaryExpressionTreeList = this
      .filterBinaryExpressionsFromAttributesList(attributeTrees);

    return this.binaryExpressionTreeList;
  }

  public int countBinaryExpressions() {
    return this.binaryExpressionTreeList.size();
  }




}

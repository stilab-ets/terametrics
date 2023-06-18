package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.PrefixExpressionTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrefixExpressionIdentifier {

  public List<PrefixExpressionTreeImpl> prefixes = new ArrayList<>();

  public PrefixExpressionIdentifier() {}

  public List<PrefixExpressionTreeImpl> filterPrefixes(
    AttributeTreeImpl attributeTree
  ) {
    ExpressionTree expressionTree = attributeTree.value();

    List<Tree> trees = ExpressionAnalyzer
      .getInstance().getAllNestedExpressions(expressionTree);

    return trees.stream()
      .filter(child -> child instanceof PrefixExpressionTreeImpl)
      .map(child -> (PrefixExpressionTreeImpl) child)
      .collect(Collectors.toList());
  }

  public List<PrefixExpressionTreeImpl> filterPrefixFromAttributesList(
    List<AttributeTreeImpl> attributeTrees
  ) {
    List<PrefixExpressionTreeImpl> prefixes = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributeTrees)  {
      prefixes.addAll(this.filterPrefixes(attribute));
    }
    return prefixes;
  }

  public List<PrefixExpressionTreeImpl> filterPrefixFromBlock(
    BlockTreeImpl blockTree
  ) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.prefixes = this.filterPrefixFromAttributesList(attributeTrees);

    return this.prefixes;
  }

  public int countPrefixes() {
    return this.prefixes.size();
  }


}

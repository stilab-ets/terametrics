package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeAccessTreeImpl;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReferenceIdentifier {

  public List<AttributeAccessTreeImpl> accessTrees =
    new ArrayList<>();

  public ReferenceIdentifier(){}

  public List<AttributeAccessTreeImpl> filterAttributesAccess
    (AttributeTreeImpl attributeTree) {

    ExpressionTree expressionTree = attributeTree.value();

    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

    return trees.stream()
      .filter(child -> child instanceof AttributeAccessTreeImpl)
      .map(child -> (AttributeAccessTreeImpl) child )
      .collect(Collectors.toList());
  }

  public List<AttributeAccessTreeImpl> filterAttributesAccessFromAttributesList(List<AttributeTreeImpl>
                                                                                  attributeTrees) {
    List<AttributeAccessTreeImpl> attributeAccessTrees = new ArrayList<>();

    for(AttributeTreeImpl attributeAccess: attributeTrees) {
      attributeAccessTrees.addAll( this.filterAttributesAccess(attributeAccess) );
    }

    return attributeAccessTrees;
  }

  public List<AttributeAccessTreeImpl> filterAttributeAccessFromBlock(BlockTreeImpl blockTree) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.accessTrees = this.filterAttributesAccessFromAttributesList(attributeTrees);

    return this.accessTrees;
  }

  public int countAttributeAccessPerBlock() {
    return this.accessTrees.size();
  }

}

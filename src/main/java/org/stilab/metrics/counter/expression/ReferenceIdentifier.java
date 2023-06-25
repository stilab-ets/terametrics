package org.stilab.metrics.counter.expression;

import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReferenceIdentifier {

  public List<TerraformTreeImpl> accessTrees =
    new ArrayList<>();

  public ReferenceIdentifier(){}

  public List<TerraformTreeImpl> filterAttributesAccess
    (AttributeTreeImpl attributeTree) {

    ExpressionTree expressionTree = attributeTree.value();

    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

    List<TerraformTreeImpl> terraformTrees = new ArrayList<>();

    for (Tree tree: trees) {
       if ((tree instanceof AttributeAccessTreeImpl) || (tree instanceof AttributeSplatAccessTreeImpl)) {
         terraformTrees.add((TerraformTreeImpl) tree);
       }
    }

    return terraformTrees;

//    return trees.stream()
//      .filter(child -> child instanceof AttributeAccessTreeImpl)
//      .map(child -> (AttributeAccessTreeImpl) child )
//      .collect(Collectors.toList());
  }

  public List<TerraformTreeImpl> filterAttributesAccessFromAttributesList(List<AttributeTreeImpl>
                                                                                  attributeTrees) {
    List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();

    for(AttributeTreeImpl attributeAccess: attributeTrees) {
      attributeAccessTrees.addAll( this.filterAttributesAccess(attributeAccess) );
    }

    return attributeAccessTrees;
  }

  public List<TerraformTreeImpl> filterAttributeAccessFromBlock(BlockTreeImpl blockTree) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.accessTrees = this.filterAttributesAccessFromAttributesList(attributeTrees);

    return this.accessTrees;
  }

  public int countAttributeAccessPerBlock() {
    return this.accessTrees.size();
  }

}

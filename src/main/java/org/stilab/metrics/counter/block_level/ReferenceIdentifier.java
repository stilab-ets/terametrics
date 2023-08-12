package org.stilab.metrics.counter.block_level;

import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;

import java.util.ArrayList;
import java.util.List;

public class ReferenceIdentifier {

  public List<TerraformTreeImpl> pointers = new ArrayList<>();
  public List<AttributeTreeImpl> attributes = new ArrayList<>();

  public ReferenceIdentifier(){}

  public List<TerraformTreeImpl> filterAttributesAccess(AttributeTreeImpl attributeTree) {

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
//    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAllAttributes(blockTree);
    attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
    pointers = this.filterAttributesAccessFromAttributesList(attributes);
    return pointers;
  }

  public int totalAttributeAccess(){
    return this.pointers.size();
  }

  public double avgAttributeAccess(){
    if (attributes.size()>0) {
      return (double) this.pointers.size() / attributes.size();
    }
    return 0.0;
  }

  public int maxAttributeAccess(){
    int max = 0;
    for (AttributeTreeImpl attribute: attributes) {
      int value = filterAttributesAccess(attribute).size();
      if (value >= max) {
        max = value;
      }
    }
    return max;
  }

}

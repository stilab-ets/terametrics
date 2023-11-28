package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.utils.spliters.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ReferenceVisitor {

    private List<TerraformTreeImpl> pointers = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      List<TerraformTreeImpl> terraformTrees = new ArrayList<>();
      for (Tree tree: trees) {
         if ((tree instanceof AttributeAccessTreeImpl) || (tree instanceof AttributeSplatAccessTreeImpl)) {
           terraformTrees.add((TerraformTreeImpl) tree);
         }
      }
      return terraformTrees;
    }

    public List<TerraformTreeImpl> filterAttributesAccessFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.visit(attributeAccess) );
      }
      return attributeAccessTrees;
    }

    public List<TerraformTreeImpl> filterAttributeAccessFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      pointers = this.filterAttributesAccessFromAttributesList(attributes);
      return pointers;
    }

    public int totalAttributeAccess(){
      return this.pointers.size();
    }

    public double avgAttributeAccess(){
      if (!attributes.isEmpty()) {
        double avgNumberOfElementsPerDifferentObjects = (double) this.pointers.size() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxAttributeAccess(){

      if (attributes.isEmpty()) { return 0; }

      int max = visit(attributes.get(0)).size();
      for (AttributeTreeImpl attribute: attributes) {
        int value = visit(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

}

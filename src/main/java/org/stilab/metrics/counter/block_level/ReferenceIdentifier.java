package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
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
    }

    public List<TerraformTreeImpl> filterAttributesAccessFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.filterAttributesAccess(attributeAccess) );
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
        return (double) this.pointers.size() / attributes.size();
      }
      return 0.0;
    }

    public int maxAttributeAccess(){

      if (attributes.isEmpty()) { return 0; }

      int max = filterAttributesAccess(attributes.get(0)).size();
      for (AttributeTreeImpl attribute: attributes) {
        int value = filterAttributesAccess(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterAttributeAccessFromBlock(identifiedBlock);
      int numReferences = this.totalAttributeAccess();
      double avgReferences = this.avgAttributeAccess();
      int maxReferences = this.maxAttributeAccess();

      metrics.put("numReferences", numReferences);
      metrics.put("avgReferences", avgReferences);
      metrics.put("maxReferences", maxReferences);

      return metrics;
    }
}
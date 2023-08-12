package org.stilab.metrics.counter.block_level;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.IndexAccessExprTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IndexAccessIdentifier {

      List<IndexAccessExprTreeImpl> indexAccess = new ArrayList<>();
      List<AttributeTreeImpl> attributes = new ArrayList<>();

      public IndexAccessIdentifier() {}

      public List<IndexAccessExprTreeImpl> identifyIndexAccess(AttributeTreeImpl attributeTree) {
        ExpressionTree expressionTree = attributeTree.value();
        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
        return trees.stream().filter(child -> child instanceof IndexAccessExprTreeImpl)
                .map(child -> (IndexAccessExprTreeImpl) child)
                .collect(Collectors.toList());
      }

      public List<IndexAccessExprTreeImpl> identifyIndexAccessFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
        List<IndexAccessExprTreeImpl> attributesAccessTrees = new ArrayList<>();
        for (AttributeTreeImpl attributeTree: attributeTrees) {
          attributesAccessTrees.addAll( this.identifyIndexAccess(attributeTree) );
        }
        return attributesAccessTrees;
      }

      public List<IndexAccessExprTreeImpl> identifyIndexAccessFromBlock(BlockTreeImpl blockTree) {
//        List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAllAttributes(blockTree);
        attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
        indexAccess = this.identifyIndexAccessFromAttributesList(attributes);
        return indexAccess;
      }

//      public int countIndexAccessExpr() {
//        return this.indexAccess.size();
//      }

      public int totalIndexAccessExpressions() {
        return indexAccess.size();
      }

      public double avgIndexAccessExpressions() {
        if (attributes.size()>0){
          return (double) totalIndexAccessExpressions() / attributes.size();
        }
        return 0.0;
      }

      public int maxIndexAccessExpressions() {
        int max = 0;
        for (AttributeTreeImpl attribute: attributes) {
          int value = identifyIndexAccess(attribute).size();
          if (value >= max) {
            max = value;
          }
        }
        return max;
      }
}

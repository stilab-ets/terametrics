package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.IndexAccessExprTreeImpl;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.block.data.repository.Repository;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IndexAccessVisitor {

      private List<IndexAccessExprTreeImpl> indexAccess = new ArrayList<>();
      private List<AttributeTreeImpl> attributes = new ArrayList<>();

      public List<IndexAccessExprTreeImpl> visit(AttributeTreeImpl attributeTree) {
        ExpressionTree expressionTree = attributeTree.value();
        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
        return trees.stream().filter(IndexAccessExprTreeImpl.class::isInstance)
                .map(IndexAccessExprTreeImpl.class::cast)
                .collect(Collectors.toList());
      }

      public List<IndexAccessExprTreeImpl> identifyIndexAccessFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
        List<IndexAccessExprTreeImpl> attributesAccessTrees = new ArrayList<>();
        for (AttributeTreeImpl attributeTree: attributeTrees) {
          attributesAccessTrees.addAll( this.visit(attributeTree) );
        }
        return attributesAccessTrees;
      }

      public List<IndexAccessExprTreeImpl> identifyIndexAccessFromBlock(BlockTreeImpl blockTree) {
        attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
        indexAccess = this.identifyIndexAccessFromAttributesList(attributes);
        return indexAccess;
      }

      public int totalIndexAccessExpressions() {
        return indexAccess.size();
      }

      public double avgIndexAccessExpressions() {
        if (!attributes.isEmpty()){
          double avgIndexAccessExpressions = (double) totalIndexAccessExpressions() / attributes.size();
          BigDecimal roundedAverage = BigDecimal.valueOf(avgIndexAccessExpressions).setScale(2, RoundingMode.HALF_UP);
          return roundedAverage.doubleValue();
        }
        return 0.0;
      }

      public int maxIndexAccessExpressions() {
        if (attributes.isEmpty()){ return 0; }

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

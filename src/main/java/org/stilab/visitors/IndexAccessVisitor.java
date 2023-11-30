package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.IndexAccessExprTreeImpl;
import org.stilab.parser.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IndexAccessVisitor {

      private List<IndexAccessExprTreeImpl> indexAccess = new ArrayList<>();
      private List<AttributeTreeImpl> attributes = new ArrayList<>();

      public List<AttributeTreeImpl> getAttributes() {
        return attributes;
      }

      public List<IndexAccessExprTreeImpl> getIndexAccess() { return indexAccess; }

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
}

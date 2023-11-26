package org.stilab.metrics.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.iterators.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TupleVisitor {

    private List<TerraformTreeImpl> tuples = new ArrayList<>();

    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      Stream<TerraformTreeImpl> localTuples = trees.stream().filter(TupleTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);
      Stream<TerraformTreeImpl> forTuples = trees.stream().filter(ForTupleTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);
      Stream<TerraformTreeImpl> combinedFilters = Stream.concat(localTuples, forTuples);
      return combinedFilters.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterTuplesFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.visit(attributeAccess) );
      }
      return attributeAccessTrees;
    }

    public List<TerraformTreeImpl> filterTuplesFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      tuples = this.filterTuplesFromAttributesList(attributes);
      return tuples;
    }

    public int totalNumberOfTuples() {
      return this.tuples.size();
    }

    public double avgNumberOfTuples() {
      if (!attributes.isEmpty()) {
        double avgNumberOfTuples = (double) totalNumberOfTuples() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfTuples).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfTuples() {
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

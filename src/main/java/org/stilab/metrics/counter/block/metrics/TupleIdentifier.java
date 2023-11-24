package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TupleIdentifier {

    public List<TerraformTreeImpl> tuples = new ArrayList<>();

    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> filterTupleIdentifier(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      Stream<TerraformTreeImpl> localTuples = trees.stream().filter(child -> child instanceof TupleTreeImpl)
        .map(child -> (TerraformTreeImpl) child );
      Stream<TerraformTreeImpl> forTuples = trees.stream().filter(child -> child instanceof ForTupleTreeImpl)
        .map(child -> (TerraformTreeImpl) child);
      Stream<TerraformTreeImpl> combinedFilters = Stream.concat(localTuples, forTuples);
      return combinedFilters.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterTuplesFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.filterTupleIdentifier(attributeAccess) );
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
      int max = filterTupleIdentifier(attributes.get(0)).size();
      for (AttributeTreeImpl attribute: attributes) {
        int value = filterTupleIdentifier(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterTuplesFromBlock(identifiedBlock);
      int numTuples = this.totalNumberOfTuples();
      double avgTuples = this.avgNumberOfTuples();
      int maxTuples = this.maxNumberOfTuples();

      metrics.put("numTuples", numTuples);
      metrics.put("avgTuples", avgTuples);
      metrics.put("maxTuples", maxTuples);

      return metrics;
    }

}

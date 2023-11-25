package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SplatExpressionVisitor {

    private List<TerraformTreeImpl> splatExpressions = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree ) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().
        getAllNestedExpressions(expressionTree);

      Stream<TerraformTreeImpl> splatAccessFilter = trees.stream()
        .filter(AttributeSplatAccessTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);

      Stream<TerraformTreeImpl> indexSplatAccess = trees.stream()
        .filter(IndexSplatAccessTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);

      Stream<TerraformTreeImpl> combinedFilters = Stream.concat(
        splatAccessFilter, indexSplatAccess
      );
      return combinedFilters.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterSplatFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> splats = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        splats.addAll(this.visit(attribute)  );
      }
      return splats;
    }

    public List<TerraformTreeImpl> filtersSplatsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      splatExpressions = this.filterSplatFromAttributesList(attributes);
      return splatExpressions;
    }

    public int totalSplatExpressions() {
      return splatExpressions.size();
    }

    public double avgSplatExpressions() {
      if (!attributes.isEmpty()) {
        double avgSplatExpressions = (double) totalSplatExpressions() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgSplatExpressions).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxSplatExpressions() {

      if (attributes.isEmpty()) { return 0;}

      int max = visit(attributes.get(0)).size();

      for(AttributeTreeImpl attribute: attributes) {
        int value = visit(attribute).size();
        if (value > max) {
          max = value;
        }
      }

      return max;
    }

}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
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

public class SplatExpressionIdentifier {

    public List<TerraformTreeImpl> splatExpressions = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> filterSplats(AttributeTreeImpl attributeTree ) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().
        getAllNestedExpressions(expressionTree);

      Stream<TerraformTreeImpl> splatAccessFilter = trees.stream()
        .filter(child -> child instanceof AttributeSplatAccessTreeImpl)
        .map(child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> indexSplatAccess = trees.stream()
        .filter(child -> child instanceof IndexSplatAccessTreeImpl)
        .map( child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> combinedFilters = Stream.concat(
        splatAccessFilter, indexSplatAccess
      );
      return combinedFilters.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterSplatFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> splats = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        splats.addAll(this.filterSplats(attribute)  );
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

      int max = filterSplats(attributes.get(0)).size();

      for(AttributeTreeImpl attribute: attributes) {
        int value = filterSplats(attribute).size();
        if (value > max) {
          max = value;
        }
      }

      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filtersSplatsFromBlock(identifiedBlock);
      int numSplatExpressions = this.totalSplatExpressions();
      double avgSplatExpressions = this.avgSplatExpressions();
      int maxSplatExpressions = this.maxSplatExpressions();

      metrics.put("numSplatExpressions", numSplatExpressions);
      metrics.put("avgSplatExpressions", avgSplatExpressions);
      metrics.put("maxSplatExpressions", maxSplatExpressions);

      return metrics;
    }
}

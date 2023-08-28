package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoopsExpressionIdentifier {

    // ForObjectTreeImpl
    // ForTupleTreeImpl
    // TemplateForDirectiveTreeImpl

    public List<TerraformTreeImpl> loops = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public LoopsExpressionIdentifier(){}

    public List<TerraformTreeImpl> filterLoops
      (AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();

      List<Tree> trees = ExpressionAnalyzer.getInstance()
        .getAllNestedExpressions(expressionTree);

      Stream<TerraformTreeImpl> forObjs = trees.stream()
        .filter(child -> child instanceof ForObjectTreeImpl)
        .map(child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> forTuples = trees.stream()
        .filter( child -> child instanceof ForTupleTreeImpl)
        .map(child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> forTemplateDirective = trees.stream()
        .filter( child -> child instanceof TemplateForDirectiveTreeImpl)
        .map(child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> combinedFiltersLevel1 = Stream.concat(forObjs, forTuples);

      Stream<TerraformTreeImpl> combinedFiltersLevel2 = Stream.concat(combinedFiltersLevel1,
        forTemplateDirective);

      return combinedFiltersLevel2.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterLoopsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> fors = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        fors.addAll(this.filterLoops(attribute));
      }
      return fors;
    }

    public List<TerraformTreeImpl> filterLoopsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      loops = this.filterLoopsFromAttributesList(attributes);
      return loops;
    }

    public int totalNumberOfLoops() {
      return this.loops.size();
    }

    public double avgNumberOfLoops(){
      if (!attributes.isEmpty()) {
        double avgNumberOfLoops = (double) totalNumberOfLoops() / attributes.size();
        BigDecimal roundedAverage = new BigDecimal(avgNumberOfLoops).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfLoops(){
      if (attributes.isEmpty()){ return 0; }

      int max = filterLoops(attributes.get(0)).size();
      for(AttributeTreeImpl attribute: attributes) {
        int tmpValue = filterLoops(attribute).size();
        if (tmpValue > max) {
          max = tmpValue;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterLoopsFromBlock(identifiedBlock);
      int numLoops = this.totalNumberOfLoops();
      double avgLoops = this.avgNumberOfLoops();
      int maxLoops = this.maxNumberOfLoops();

      metrics.put("numLoops", numLoops);
      metrics.put("avgLoops", avgLoops);
      metrics.put("maxLoops", maxLoops);

      return metrics;
    }

}

package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.block.data.repository.Repository;
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

public class LoopsExpressionVisitor {

    // ForObjectTreeImpl
    // ForTupleTreeImpl
    // TemplateForDirectiveTreeImpl

    private List<TerraformTreeImpl> loops = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();

      List<Tree> trees = ExpressionAnalyzer.getInstance()
        .getAllNestedExpressions(expressionTree);

      Stream<TerraformTreeImpl> forObjs = trees.stream()
        .filter(ForObjectTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);

      Stream<TerraformTreeImpl> forTuples = trees.stream()
        .filter(ForTupleTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);

      Stream<TerraformTreeImpl> forTemplateDirective = trees.stream()
        .filter(TemplateForDirectiveTreeImpl.class::isInstance)
        .map(TerraformTreeImpl.class::cast);

      Stream<TerraformTreeImpl> combinedFiltersLevel1 = Stream.concat(forObjs, forTuples);

      Stream<TerraformTreeImpl> combinedFiltersLevel2 = Stream.concat(combinedFiltersLevel1,
        forTemplateDirective);

      return combinedFiltersLevel2.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterLoopsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> fors = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        fors.addAll(this.visit(attribute));
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
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfLoops).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfLoops(){
      if (attributes.isEmpty()){ return 0; }

      int max = visit(attributes.get(0)).size();
      for(AttributeTreeImpl attribute: attributes) {
        int tmpValue = visit(attribute).size();
        if (tmpValue > max) {
          max = tmpValue;
        }
      }
      return max;
    }


}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.sonar.iac.common.api.tree.Tree;
import org.stilab.utils.spliters.ExpressionAnalyzer;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionalExpressionIdentifier {

    public List<TerraformTreeImpl> conditions = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();
    public ConditionalExpressionIdentifier(){}
    public List<TerraformTreeImpl> filterConditions(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      Stream<TerraformTreeImpl> conditionFilter = trees.stream()
                                                    .filter(child -> child instanceof ConditionTreeImpl)
                                                    .map(child -> (TerraformTreeImpl) child);
      Stream<TerraformTreeImpl> ifDirectiveFilter = trees.stream()
                                                    .filter(child -> child instanceof TemplateIfDirectiveTreeImpl)
                                                    .map(child -> (TerraformTreeImpl) child);
      List<TerraformTreeImpl> combinedFilters = Stream.concat(conditionFilter, ifDirectiveFilter).collect(Collectors.toList());
      List<SyntaxTokenImpl> tokens = identifyTokens(attributeTree);
      combinedFilters.addAll(tokens);
      return combinedFilters;
    }

    public List<SyntaxTokenImpl> identifyTokens(AttributeTreeImpl attributeTree) {
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(attributeTree.value());
      List<SyntaxTokenImpl> syntaxTokens = trees.stream()
        .filter(tree -> tree instanceof SyntaxTokenImpl)
        .map(tree -> (SyntaxTokenImpl) tree)
        .filter(token -> token.value().equals("if") )
        .collect(Collectors.toList());
      return syntaxTokens;
    }

    public List<TerraformTreeImpl> filterConditionsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> directives = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        directives.addAll(this.filterConditions(attribute));
      }
      return directives;
    }

    public List<TerraformTreeImpl> filtersConditionsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      conditions = this.filterConditionsFromAttributesList(attributes);
      return conditions;
    }

    public int totalNumberOfConditions(){
      return this.conditions.size();
    }

    public double avgNumberOfConditionsPerAttribute(){
      if (!attributes.isEmpty()){
        double avgNumberOfConditionsPerAttribute = (double) conditions.size()  / attributes.size();
        BigDecimal roundedAverage = new BigDecimal(avgNumberOfConditionsPerAttribute).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfConditionsPerAttribute(){

      if (attributes.isEmpty()){ return 0; }

      int max = filterConditions(attributes.get(0)).size();
      for (AttributeTreeImpl attributeTree: attributes){
         int tmpValue = filterConditions(attributeTree).size();
         if (max < tmpValue ){
           max = tmpValue;
         }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filtersConditionsFromBlock(identifiedBlock);
      int numConditions = this.totalNumberOfConditions();
      int maxConditionsPerAttr = this.maxNumberOfConditionsPerAttribute();
      double avgConditionsPerAttr = this.avgNumberOfConditionsPerAttribute();

      metrics.put("numConditions", numConditions);
      metrics.put("avgConditions", avgConditionsPerAttr);
      metrics.put("maxConditions",maxConditionsPerAttr);

      return metrics;
    }
}

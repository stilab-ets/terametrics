package org.stilab.metrics.counter.block_level;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.sonar.iac.common.api.tree.Tree;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

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

    List<Tree> trees = ExpressionAnalyzer.getInstance()
      .getAllNestedExpressions(expressionTree);

    Stream<TerraformTreeImpl> conditionFilter = trees.stream()
                                                  .filter(child -> child instanceof ConditionTreeImpl)
                                                  .map(child -> (TerraformTreeImpl) child);

    Stream<TerraformTreeImpl> ifDirectiveFilter = trees.stream()
                                                  .filter(child -> child instanceof TemplateIfDirectiveTreeImpl)
                                                  .map(child -> (TerraformTreeImpl) child);
    Stream<TerraformTreeImpl> combinedFilters = Stream.concat(conditionFilter, ifDirectiveFilter);

    return combinedFilters.collect(Collectors.toList());
  }

  public List<TerraformTreeImpl> filterConditionsFromAttributesList(
    List<AttributeTreeImpl> attributeTrees) {

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

  public int countConditions() {
    return this.conditions.size();
  }

  public int totalNumberOfConditions(){
    return this.conditions.size();
  }

  public double avgNumberOfConditionsPerAttribute(){
    if ( attributes.size() > 0 ){
      return (double) conditions.size()  / attributes.size();
    }
    return 0.0;
  }

  public int maxNumberOfConditionsPerAttribute(){
    int max = 0;
    for (AttributeTreeImpl attributeTree: attributes){
       int tmpValue = filterConditions(attributeTree).size();
       if (max <= tmpValue ){
         max = tmpValue;
       }
    }
    return max;
  }
}

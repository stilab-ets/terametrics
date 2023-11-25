package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.block.data.repository.Repository;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogicalOperationsVisitor {

  //    Decisions Numbers
  private List<String> operators = new ArrayList<>(Arrays.asList("!", "&&", "||"));
  private List<TerraformTreeImpl> decisions = new ArrayList<>();
  private List<AttributeTreeImpl> attributes = new ArrayList<>();
  public List<TerraformTreeImpl> visit(AttributeTreeImpl attribute){
    ExpressionTree expressionTree = attribute.value();
    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

    Stream<TerraformTreeImpl> unaryOperations = trees
                              .stream()
                              .filter(PrefixExpressionTreeImpl.class::isInstance)
                              .filter(child -> operators.contains(
                                ((PrefixExpressionTreeImpl) child).prefix().value()
                              )).map(TerraformTreeImpl.class::cast);

    Stream<TerraformTreeImpl> binaryOperations = trees
                              .stream()
                              .filter(BinaryExpressionTreeImpl.class::isInstance)
                              .filter(child -> operators.contains(
                                ((BinaryExpressionTreeImpl) child).operator().value()
                              )).map(TerraformTreeImpl.class::cast);

    Stream<TerraformTreeImpl> combinedFilters = Stream.concat(unaryOperations, binaryOperations);

    return combinedFilters.collect(Collectors.toList());
  }

  public List<TerraformTreeImpl> filterLogicalOperationsFromAttributesList(List<AttributeTreeImpl> attributes) {
    List<TerraformTreeImpl> localDecisions = new ArrayList<>();
    for (AttributeTreeImpl attributeTree: attributes) {
      localDecisions.addAll(this.visit(attributeTree));
    }
    return localDecisions;
  }

  public List<TerraformTreeImpl> filterLogicalOperationsFromBlock(BlockTreeImpl blockTree){
    attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
    decisions = this.filterLogicalOperationsFromAttributesList(attributes);
    return decisions;
  }

  public int totalNumberOfLogicalOperations(){
    return this.decisions.size();
  }

  public double avgNumberOfLogicalOperations(){
    if(!attributes.isEmpty()){
      double avgNumberOfLogicalOperations = (double) totalNumberOfLogicalOperations() / attributes.size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfLogicalOperations).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfLogicalOperations(){

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

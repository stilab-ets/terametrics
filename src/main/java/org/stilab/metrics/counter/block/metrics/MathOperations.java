package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MathOperations {

//    Math Operations
//    PrefixExpressionTreeImpl -----> -
//    BinaryExpressionTreeImpl -----> +,-,*,/,%

  public List<String> operators = new ArrayList<>(Arrays.asList("-", "+", "*", "/", "%"));
  public List<TerraformTreeImpl> mathOperators = new ArrayList<>();
  public List<AttributeTreeImpl> attributes = new ArrayList<>();

  public List<TerraformTreeImpl> identifyMathOperations(AttributeTreeImpl attribute) {
    ExpressionTree expressionTree = attribute.value();
    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
    Stream<TerraformTreeImpl> unaryOperations = trees
      .stream()
      .filter(child -> child instanceof PrefixExpressionTreeImpl)
      .filter(child -> operators.contains(
        ((PrefixExpressionTreeImpl) child).prefix().value()
      )).map(child -> (TerraformTreeImpl) child);
    Stream<TerraformTreeImpl> binaryOperations = trees
      .stream()
      .filter(child -> child instanceof BinaryExpressionTreeImpl)
      .filter(child -> operators.contains(
        ((BinaryExpressionTreeImpl) child).operator().value()
      )).map(child -> (TerraformTreeImpl) child);

    Stream<TerraformTreeImpl> combinedFilters = Stream.concat(unaryOperations, binaryOperations);

    return combinedFilters.collect(Collectors.toList());

  }

  public List<TerraformTreeImpl> filterMathOperationsFromAttributesList(List<AttributeTreeImpl> attributes) {
    List<TerraformTreeImpl> operations = new ArrayList<>();
    for (AttributeTreeImpl attributeTree: attributes) {
      operations.addAll(this.identifyMathOperations(attributeTree));
    }
    return operations;
  }

  public List<TerraformTreeImpl> filterMathOperationsFromBlock(BlockTreeImpl blockTree){
    attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
    mathOperators = this.filterMathOperationsFromAttributesList(attributes);
    return mathOperators;
  }

  public int totalNumberOfMathOperation(){
    return this.mathOperators.size();
  }

  public double avgNumberOfMathOperation() {
    if (!attributes.isEmpty()) {
      double avgNumberOfMathOperation = (double) totalNumberOfMathOperation() / attributes.size();
      BigDecimal roundedAverage = new BigDecimal(avgNumberOfMathOperation).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfMathOperation(){
    if (attributes.isEmpty()){ return 0;}

    int max = identifyMathOperations(attributes.get(0)).size();
    for (AttributeTreeImpl attribute: attributes){
      int value = identifyMathOperations(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    this.filterMathOperationsFromBlock(identifiedBlock);
    int numMathOperations = this.totalNumberOfMathOperation();
    double avgMathOperations = this.avgNumberOfMathOperation();
    int maxMathOperations = this.maxNumberOfMathOperation();

    metrics.put("numMathOperations", numMathOperations);
    metrics.put("avgMathOperations", avgMathOperations);
    metrics.put("maxMathOperations", maxMathOperations);

    return metrics;

  }

}

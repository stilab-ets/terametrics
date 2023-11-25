package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MathOperationsVisitor {

//    Math Operations
//    PrefixExpressionTreeImpl -----> -
//    BinaryExpressionTreeImpl -----> +,-,*,/,%

  private List<String> operators = new ArrayList<>(Arrays.asList("-", "+", "*", "/", "%"));
  private List<TerraformTreeImpl> mathOperators = new ArrayList<>();
  private List<AttributeTreeImpl> attributes = new ArrayList<>();

  public List<TerraformTreeImpl> visit(AttributeTreeImpl attribute) {
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

  public List<TerraformTreeImpl> filterMathOperationsFromAttributesList(List<AttributeTreeImpl> attributes) {
    List<TerraformTreeImpl> operations = new ArrayList<>();
    for (AttributeTreeImpl attributeTree: attributes) {
      operations.addAll(this.visit(attributeTree));
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
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfMathOperation).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfMathOperation(){
    if (attributes.isEmpty()){ return 0;}

    int max = visit(attributes.get(0)).size();
    for (AttributeTreeImpl attribute: attributes){
      int value = visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

}

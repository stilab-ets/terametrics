package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.parser.spliters.ExpressionAnalyzer;

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


  public List<AttributeTreeImpl> getAttributes() {
    return attributes;
  }

  public List<String> getOperators() {
    return operators;
  }

  public List<TerraformTreeImpl> getDecisions() {
    return decisions;
  }

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



}

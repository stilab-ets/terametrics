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

public class ComparisonOperatorsVisitor{

    private List<String> operators = new ArrayList<>(Arrays.asList("==", "!=", "<", ">", "<=", ">="));
    private List<BinaryExpressionTreeImpl> comparisonOperations = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }

    public List<String> getOperators() {
      return operators;
    }

    public List<BinaryExpressionTreeImpl> getComparisonOperations() {
      return comparisonOperations;
    }

    public List<BinaryExpressionTreeImpl> visit(AttributeTreeImpl attribute) {

        ExpressionTree expressionTree = attribute.value();

        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

        Stream<BinaryExpressionTreeImpl> binaryOperations = trees
          .stream()
          .filter(BinaryExpressionTreeImpl.class::isInstance)
          .filter(child -> operators.contains(
            ((BinaryExpressionTreeImpl) child).operator().value()
          )).map(BinaryExpressionTreeImpl.class::cast);

        return binaryOperations.collect(Collectors.toList());
    }

    public List<BinaryExpressionTreeImpl> filterComparisonOperatorsFromAttributesList(
      List<AttributeTreeImpl>  attributes) {
      List<BinaryExpressionTreeImpl> operations = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributes) {
        operations.addAll(this.visit(attributeTree));
      }
      return operations;
    }

    public List<BinaryExpressionTreeImpl> filterComparisonOperatorsFromBlock(BlockTreeImpl blockTree){
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      comparisonOperations = this.filterComparisonOperatorsFromAttributesList(attributes);
      return comparisonOperations;
    }



}

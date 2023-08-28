package org.stilab.metrics.counter.block_level;

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

public class ComparisonOperatorsIdentifier {

    public ComparisonOperatorsIdentifier() {}
    public List<String> operators = new ArrayList<>(Arrays.asList("==", "!=", "<", ">", "<=", ">="));
    public List<BinaryExpressionTreeImpl> comparisonOperations = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<BinaryExpressionTreeImpl> identifyComparisonOperators(AttributeTreeImpl attribute) {

      ExpressionTree expressionTree = attribute.value();

      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

      Stream<BinaryExpressionTreeImpl> binaryOperations = trees
        .stream()
        .filter(child -> child instanceof BinaryExpressionTreeImpl)
        .filter(child -> operators.contains(
          ((BinaryExpressionTreeImpl) child).operator().value()
        ))
        .map(child -> (BinaryExpressionTreeImpl) child);

      return binaryOperations.collect(Collectors.toList());
    }

    public List<BinaryExpressionTreeImpl> filterComparisonOperatorsFromAttributesList(List<AttributeTreeImpl>
                                                                                 attributes) {
      List<BinaryExpressionTreeImpl> operations = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributes) {
        operations.addAll(this.identifyComparisonOperators(attributeTree));
      }
      return operations;
    }

    public List<BinaryExpressionTreeImpl> filterComparisonOperatorsFromBlock(BlockTreeImpl blockTree){
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      comparisonOperations = this.filterComparisonOperatorsFromAttributesList(attributes);
      return comparisonOperations;
    }

    public int totalNumberOfComparisonOperation(){
      return this.comparisonOperations.size();
    }

    public double avgNumberOfComparisonOperation(){
      if (!attributes.isEmpty()){
        double avgNumberOfComparisonOperation = (double) totalNumberOfComparisonOperation() / attributes.size();
        BigDecimal roundedAverage = new BigDecimal(avgNumberOfComparisonOperation).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfComparisonOperation(){
      if (attributes.isEmpty()){ return 0; }
      int max = identifyComparisonOperators(attributes.get(0)).size();
      for(AttributeTreeImpl attribute: attributes) {
        int value = identifyComparisonOperators(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      this.filterComparisonOperatorsFromBlock(identifiedBlock);
      int numComparisonOperators = this.totalNumberOfComparisonOperation();
      double avgComparisonOperators = this.avgNumberOfComparisonOperation();
      int maxComparisonOperators = this.maxNumberOfComparisonOperation();

      metrics.put("numComparisonOperators", numComparisonOperators);
      metrics.put("avgComparisonOperators", avgComparisonOperators);
      metrics.put("maxComparisonOperators", maxComparisonOperators);

      return metrics;
    }

}

package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TemplateExpressionTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateExpressionIdentifier {

    public List<TemplateExpressionTreeImpl> templateExpressionPerBlock = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public TemplateExpressionIdentifier(){}

    public List<TemplateExpressionTreeImpl> filterTemplateExpression(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
        .filter(child -> child instanceof TemplateExpressionTreeImpl)
        .map(child -> (TemplateExpressionTreeImpl) child)
        .collect(Collectors.toList());
    }

    public List<TemplateExpressionTreeImpl> filterTemplateExpressionsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TemplateExpressionTreeImpl> templateExpressions = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributeTrees) {
        templateExpressions.addAll( this.filterTemplateExpression(attributeTree) );
      }
      return templateExpressions;
    }

    public List<TemplateExpressionTreeImpl> filterTemplateExpressionsFromBlock(BlockTreeImpl blockTree) {
      attributes =  (new AttrFinderImpl()).getAllAttributes(blockTree);
      templateExpressionPerBlock = this.filterTemplateExpressionsFromAttributesList(attributes);
      return templateExpressionPerBlock;
    }

    public int totalNumberOfTemplateExpressionsPerBlock() {
      return templateExpressionPerBlock.size();
    }

    public double avgNumOfTemplateExpressionPerBlock() {
      if (!attributes.isEmpty()) {
        double avgNumOfTemplateExpressionPerBlock = (double) totalNumberOfTemplateExpressionsPerBlock() / attributes.size();
        BigDecimal roundedAverage = new BigDecimal(avgNumOfTemplateExpressionPerBlock).setScale(2,
          RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
  }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterTemplateExpressionsFromBlock(identifiedBlock);
      int numTemplateExpression = this.totalNumberOfTemplateExpressionsPerBlock();
      double avgNumTemplateExpression = avgNumOfTemplateExpressionPerBlock();

      metrics.put("numTemplateExpression", numTemplateExpression);
      metrics.put("avgTemplateExpression", avgNumTemplateExpression);

      return metrics;
    }

}

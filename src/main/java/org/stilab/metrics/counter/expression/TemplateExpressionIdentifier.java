package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TemplateExpressionTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateExpressionIdentifier {




  public List<TemplateExpressionTreeImpl> templateExpressionPerBlock =
    new ArrayList<>();

  public TemplateExpressionIdentifier(){}


  public List<TemplateExpressionTreeImpl> filterTemplateExpression
    (AttributeTreeImpl attributeTree) {

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
    List<AttributeTreeImpl> attributeTrees =  (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    templateExpressionPerBlock = this.filterTemplateExpressionsFromAttributesList(attributeTrees);

    return templateExpressionPerBlock;
  }

  public int countTemplateExpressionsPerBlock() {
    return templateExpressionPerBlock.size();
  }


}

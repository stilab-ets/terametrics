package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.stilab.parser.spliters.ExpressionAnalyzer;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionalExpressionVisitor{

    private List<TerraformTreeImpl> conditions = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> getConditions() {
      return conditions;
    }

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree) {
        ExpressionTree expressionTree = attributeTree.value();
        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
        Stream<TerraformTreeImpl> conditionFilter = trees.stream()
                                                      .filter(ConditionTreeImpl.class::isInstance)
                                                      .map(TerraformTreeImpl.class::cast);
        Stream<TerraformTreeImpl> ifDirectiveFilter = trees.stream()
                                                      .filter(TemplateIfDirectiveTreeImpl.class::isInstance)
                                                      .map(TerraformTreeImpl.class::cast);
        List<TerraformTreeImpl> combinedFilters = Stream.concat(conditionFilter, ifDirectiveFilter).collect(Collectors.toList());
        List<SyntaxTokenImpl> tokens = identifyTokens(attributeTree);
        combinedFilters.addAll(tokens);
        return combinedFilters;
      }

    public List<SyntaxTokenImpl> identifyTokens(AttributeTreeImpl attributeTree) {
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(attributeTree.value());
      return trees.stream()
        .filter(SyntaxTokenImpl.class::isInstance)
        .map(SyntaxTokenImpl.class::cast)
        .filter(token -> token.value().equals("if") )
        .collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterConditionsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> directives = new ArrayList<>();
      for (AttributeTreeImpl attribute: attributeTrees) {
        directives.addAll(this.visit(attribute));
      }
      return directives;
    }

    public List<TerraformTreeImpl> filtersConditionsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      conditions = this.filterConditionsFromAttributesList(attributes);
      return conditions;
    }



}

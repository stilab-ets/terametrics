package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.VariableExprTreeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VariablesIdentifier {

  public List<VariableExprTreeImpl> variables =
    new ArrayList<>();

  public VariablesIdentifier(){}

  public List<VariableExprTreeImpl> filterVariablesExpr
    (AttributeTreeImpl attributeTree) {

    ExpressionTree expressionTree = attributeTree.value();

    List<Tree> trees = ExpressionAnalyzer.getInstance()
      .getAllNestedExpressions(expressionTree);

    return trees.stream()
      .filter(child -> child instanceof VariableExprTreeImpl)
      .map(child -> (VariableExprTreeImpl) child)
      .collect(Collectors.toList());
  }

  public List<VariableExprTreeImpl> filterVarsFromAttributesList(
    List<AttributeTreeImpl> attributeTrees
  ) {
    List<VariableExprTreeImpl> vars = new ArrayList<>();

    for (AttributeTreeImpl attributeTree: attributeTrees) {
      vars.addAll(this.filterVariablesExpr(attributeTree));
    }

    return vars;
  }

  public List<VariableExprTreeImpl> filterVarsFromBlock(BlockTreeImpl blockTree) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.variables = this.filterVarsFromAttributesList(attributeTrees);

    return this.variables;
  }

  public int countVars(){
    return this.variables.size();
  }

}

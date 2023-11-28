package org.stilab.metrics.visitors;

import org.stilab.utils.spliters.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.VariableExprTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VariablesVisitor {

    private List<VariableExprTreeImpl> variables = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<VariableExprTreeImpl> visit(AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
        .filter(VariableExprTreeImpl.class::isInstance)
        .map(VariableExprTreeImpl.class::cast)
        .collect(Collectors.toList());

    }

    public List<VariableExprTreeImpl> filterVarsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<VariableExprTreeImpl> vars = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributeTrees) {
        vars.addAll(this.visit(attributeTree));
      }
      return vars;
    }

    public List<VariableExprTreeImpl> filterVarsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      this.variables = this.filterVarsFromAttributesList(attributes);
      return this.variables;
    }

    public int totalNumberOfVars(){
      return this.variables.size();
    }

    public double avgNumberOfVars(){
      if (!attributes.isEmpty()) {
        double avgNumberOfElementsPerDifferentObjects = (double) totalNumberOfVars() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfVars(){
      if (attributes.isEmpty()){ return 0; }

      int max = visit(attributes.get(0)).size();
      for (AttributeTreeImpl attribute: attributes){
        int value = visit(attribute).size();
        if ( value > max ){
          max = value;
        }
      }
      return max;
    }

}

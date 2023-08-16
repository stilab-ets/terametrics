package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
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

    public List<VariableExprTreeImpl> variables = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public VariablesIdentifier(){}

    public List<VariableExprTreeImpl> filterVariablesExpr(AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
        .filter(child -> child instanceof VariableExprTreeImpl)
        .map(child -> (VariableExprTreeImpl) child)
        .collect(Collectors.toList());

    }

    public List<VariableExprTreeImpl> filterVarsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<VariableExprTreeImpl> vars = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributeTrees) {
        vars.addAll(this.filterVariablesExpr(attributeTree));
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
        return (double) totalNumberOfVars() / attributes.size();
      }
      return 0.0;
    }

    public int maxNumberOfVars(){
      if (attributes.isEmpty()){ return 0; }

      int max = filterVariablesExpr(attributes.get(0)).size();;
      for (AttributeTreeImpl attribute: attributes){
        int value = filterVariablesExpr(attribute).size();
        if ( value > max ){
          max = value;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterVarsFromBlock(identifiedBlock);
      int numVars = this.totalNumberOfVars();
      double avgNumVars = this.avgNumberOfVars();
      int maxNumVars = this.maxNumberOfVars();

      metrics.put("numVars", numVars);
      metrics.put("avgNumVars", avgNumVars);
      metrics.put("maxNumVars", maxNumVars);

      return metrics;
    }
}

package org.stilab.metrics.visitors;

import org.stilab.metrics.iterators.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallExpressionVisitor {

    private List<FunctionCallTreeImpl> functionsCallPerBlock = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<FunctionCallTreeImpl> visit(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
                  .filter(FunctionCallTreeImpl.class::isInstance)
                  .map(FunctionCallTreeImpl.class::cast)
                  .collect(Collectors.toList());
    }

    public List<FunctionCallTreeImpl> filterFCfromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributeTrees) {
        functionCallTrees.addAll( this.visit(attributeTree) );
      }
      return functionCallTrees;
    }

//    root blocks
    public List<FunctionCallTreeImpl> filterFCfromBlock(BlockTreeImpl blockTree) {
       attributes =  (new AttrFinderImpl())
        .getAllAttributes(blockTree);
      functionsCallPerBlock = this.filterFCfromAttributesList(attributes);
      return functionsCallPerBlock;
    }

//    number of function call
    public int totalNumberOfFunctionCall() {
      return functionsCallPerBlock.size();
    }

    public double avgNumberOfFunctionCall(){

      if (!attributes.isEmpty()) {
        double avgNumberOfFunctionCall = (double) functionsCallPerBlock.size() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfFunctionCall).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }

      return 0.0;
    }

    public int maxNumberOfFunctionCall(){
      if (attributes.isEmpty()){ return 0; }

      int max = visit(attributes.get(0)).size();
      for(AttributeTreeImpl attribute: attributes) {
        int value = visit(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

}

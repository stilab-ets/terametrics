package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
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

public class FunctionCallExpressionIdentifier {

    public List<FunctionCallTreeImpl> functionsCallPerBlock = new ArrayList<>();
    public List<AttributeTreeImpl> attributes = new ArrayList<>();

    public FunctionCallExpressionIdentifier()  {}

    public List<FunctionCallTreeImpl> filterFunctionCall(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
                  .filter(child -> child instanceof FunctionCallTreeImpl)
                  .map(child -> (FunctionCallTreeImpl) child)
                  .collect(Collectors.toList());
    }

    public List<FunctionCallTreeImpl> filterFCfromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();
      for (AttributeTreeImpl attributeTree: attributeTrees) {
        functionCallTrees.addAll( this.filterFunctionCall(attributeTree) );
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

      if (attributes.size()>0) {
        double avgNumberOfFunctionCall = (double) functionsCallPerBlock.size() / attributes.size();
        BigDecimal roundedAverage = new BigDecimal(avgNumberOfFunctionCall).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }

      return 0.0;
    }

    public int maxNumberOfFunctionCall(){
      if (attributes.isEmpty()){ return 0; }

      int max = filterFunctionCall(attributes.get(0)).size();
      for(AttributeTreeImpl attribute: attributes) {
        int value = filterFunctionCall(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      this.filterFCfromBlock(identifiedBlock);
      int numFunctionCall = this.totalNumberOfFunctionCall();
      double avgFunctionCall = this.avgNumberOfFunctionCall();
      int maxFunctionCall = this.maxNumberOfFunctionCall();
      metrics.put("numFunctionCall", numFunctionCall);
      metrics.put("avgFunctionCall", avgFunctionCall);
      metrics.put("maxFunctionCall", maxFunctionCall);
      return metrics;
    }

}

package org.stilab.visitors;

import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FunctionParametersVisitor {

    List<ExpressionTree> parameters = new ArrayList<>();
    List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();
    FunctionCallExpressionVisitor functionCallExpressionVisitor;
    public FunctionParametersVisitor() {
      functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
    }

    public List<ExpressionTree> identifyFunctionParameters(FunctionCallTreeImpl functionCallTree)  {
      return functionCallTree.arguments().trees();
    }

    public List<ExpressionTree> identifyUsedParametersInBlock(BlockTreeImpl blockTree) {
      functionCallTrees = functionCallExpressionVisitor.filterFCfromBlock( blockTree );
      for ( FunctionCallTreeImpl fc: functionCallTrees ) {
        parameters.addAll(this.identifyFunctionParameters(fc) );
      }
      return parameters;
    }

    //  Total Number Of Parameters Per Block
    public int totalNumberParamsPerBlock() {
      return this.parameters.size();
    }

    //  Avg Number Of Parameters Per Block
    public double avgNumberParamsPerBlock() {
      if (!this.functionCallTrees.isEmpty()) {
        double avgNumberParamsPerBlock = (double) totalNumberParamsPerBlock() / this.functionCallTrees.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberParamsPerBlock).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    //  Max Number Of Parameters Per Block
    public int maxNumberParamsPerBlock() {

      if (functionCallTrees.isEmpty()){ return 0; }
      int max = functionCallTrees.get(0).arguments().trees().size();
      for (FunctionCallTreeImpl functionCallTree: this.functionCallTrees) {
        int numParamsPerFunction = functionCallTree.arguments().trees().size();
        if (max < numParamsPerFunction) {
          max = numParamsPerFunction;
        }
      }

      return max;
    }

}

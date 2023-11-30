package org.stilab.calculators;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.NestedBlockVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NestedBlockCalculator {

  private NestedBlockVisitor nestedBlockVisitor;

  public NestedBlockCalculator(BlockTreeImpl identifiedBlock){
    nestedBlockVisitor = new NestedBlockVisitor();
    nestedBlockVisitor.identifyNestedBlock(identifiedBlock);
  }

  public int countNestedBlock() {
    return nestedBlockVisitor.getNestedBlocks().size();
  }



  public int depthOfBlock(BlockTreeImpl identifiedBlock) {
    int startLine    = identifiedBlock.value().textRange().start().line();
    int endLine      = identifiedBlock.value().textRange().end().line();
    return endLine - startLine + 1;
  }

  public double avgDepthNestedBlocks(){
    int sum = 0;
    for (BlockTreeImpl nstdBlock: nestedBlockVisitor.getNestedBlocks()) {
      int dep = depthOfBlock(nstdBlock);
      sum += dep;
    }
    if (!nestedBlockVisitor.getNestedBlocks().isEmpty()) {
      double averageDepth = (double) sum / nestedBlockVisitor.getNestedBlocks().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(averageDepth).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxDepthNestedBlocks(){
    int maxDepth = 0; // Initialize maxDepth to 0 or some appropriate minimum value

    for (BlockTreeImpl nstdBlock: nestedBlockVisitor.getNestedBlocks()) {
      int dep = depthOfBlock(nstdBlock);
      if (dep > maxDepth) {
        maxDepth = dep;
      }
    }
    return maxDepth;
  }

  public int minDepthNestedBlocks(){
    int minDepth = Integer.MAX_VALUE; // Initialize minDepth to a large value

    for (BlockTreeImpl nstdBlock: nestedBlockVisitor.getNestedBlocks()) {
      int dep = depthOfBlock(nstdBlock);
      if (dep < minDepth) {
        minDepth = dep;
      }
    }

    if (minDepth == Integer.MAX_VALUE) {
      return 0; // Return 0.0 if there are no nested blocks
    }

    return minDepth;
  }

}

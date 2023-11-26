package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.tree.impl.BodyTreeImpl;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NestedBlockVisitor {

    private List<BlockTreeImpl> nestedBlocks = new ArrayList<>();

    public List<BlockTreeImpl> identifyNestedBlock(BlockTreeImpl blockTree) {
      BodyTreeImpl bodyTree = (BodyTreeImpl) blockTree.value();
      nestedBlocks = this.getAllNestedBlocks(bodyTree);
      return nestedBlocks;
    }
    public int countNestedBlock() {
      return nestedBlocks.size();
    }

    public List<BlockTreeImpl> getAllNestedBlocks(Tree tree) {

      List<BlockTreeImpl> localNstdBlocks = new ArrayList<>();

      if (tree != null) {
        if (tree instanceof BlockTreeImpl) {
          localNstdBlocks.add((BlockTreeImpl) tree);
        }
        List<Tree> children = tree.children();
        for (Tree child : children) {
          localNstdBlocks.addAll(getAllNestedBlocks(child));
        }
      }
      return localNstdBlocks;
    }

    public int depthOfBlock(BlockTreeImpl identifiedBlock) {
      int startLine    = identifiedBlock.value().textRange().start().line();
      int endLine      = identifiedBlock.value().textRange().end().line();
      return endLine - startLine + 1;
    }

    public double avgDepthNestedBlocks(List<BlockTreeImpl> nestedBlocks){
      int sum = 0;
      for (BlockTreeImpl nstdBlock: nestedBlocks) {
        int dep = depthOfBlock(nstdBlock);
        sum += dep;
      }
      if (!nestedBlocks.isEmpty()) {
        double averageDepth = (double) sum / nestedBlocks.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(averageDepth).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxDepthNestedBlocks(List<BlockTreeImpl> nestedBlocks){
      int maxDepth = 0; // Initialize maxDepth to 0 or some appropriate minimum value

      for (BlockTreeImpl nstdBlock: nestedBlocks) {
        int dep = depthOfBlock(nstdBlock);
        if (dep > maxDepth) {
          maxDepth = dep;
        }
      }
      return maxDepth;
    }

    public int minDepthNestedBlocks(List<BlockTreeImpl> nestedBlocks){
      int minDepth = Integer.MAX_VALUE; // Initialize minDepth to a large value

      for (BlockTreeImpl nstdBlock: nestedBlocks) {
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

package org.stilab.visitors;

import org.sonar.iac.terraform.tree.impl.BodyTreeImpl;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class NestedBlockVisitor {

    private List<BlockTreeImpl> nestedBlocks = new ArrayList<>();

    public List<BlockTreeImpl> getNestedBlocks() {
      return nestedBlocks;
    }

    public List<BlockTreeImpl> identifyNestedBlock(BlockTreeImpl blockTree) {
        BodyTreeImpl bodyTree = (BodyTreeImpl) blockTree.value();
        nestedBlocks = this.getAllNestedBlocks(bodyTree);
        return nestedBlocks;
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


}

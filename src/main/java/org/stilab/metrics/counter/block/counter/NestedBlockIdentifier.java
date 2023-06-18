package org.stilab.metrics.counter.block.counter;

import org.sonar.iac.terraform.tree.impl.BodyTreeImpl;
import org.stilab.metrics.counter.block.finder.BodyTreeFinder;
import org.stilab.interfaces.BlockTypeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class NestedBlockIdentifier implements BlockTypeCounter {

  public List<BlockTreeImpl> nestedBlock = new ArrayList<>();

  @Override
  public int blockTypeCounter(Tree tree) {
    Tree bodyTree = (new BodyTreeFinder()).find(tree);
    return this.getAllNestedBlocks(bodyTree).size();
  }


  public List<BlockTreeImpl> identifyNestedBlock(BlockTreeImpl blockTree) {
    BodyTreeImpl bodyTree = (BodyTreeImpl) blockTree.value();
    nestedBlock = this.getAllNestedBlocks(bodyTree);
    return nestedBlock;
  }

  public int countNestedBlock() {
    return nestedBlock.size();
  }

  public List<BlockTreeImpl> getAllNestedBlocks(Tree tree) {

    List<BlockTreeImpl> nestedBlocks = new ArrayList<>();

    if (tree != null) {
      if (tree instanceof BlockTreeImpl) {
//        System.out.println(((BlockTreeImpl)tree).key().value());
        nestedBlocks.add((BlockTreeImpl) tree);
      }
      List<Tree> children = tree.children();
      for (Tree child : children) {
        nestedBlocks.addAll(getAllNestedBlocks(child));
      }
    }
    return nestedBlocks;
  }

}

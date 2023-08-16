package org.stilab.metrics.counter.block.counter;

import org.stilab.interfaces.BlockTypeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public class AllBlockFileImpl implements BlockTypeCounter {

  @Override
  public int blockTypeCounter(Tree tree) {
    TopBlockFileCounter topBlockFileCounter = new TopBlockFileCounter();
    int topBlocks = topBlockFileCounter.blockTypeCounter(tree);
    NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
    int countNested = 0;
    for (Tree tree1 : tree.children()) {
      if (tree1 instanceof BlockTreeImpl) {
        countNested += nestedBlockCounter.blockTypeCounter(tree1);
      }
    }
    return  countNested + topBlocks ;
  }
}

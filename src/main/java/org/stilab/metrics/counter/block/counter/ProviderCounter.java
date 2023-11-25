package org.stilab.metrics.counter.block.counter;

import org.stilab.metrics.counter.interfaces.BlockCheckerType;
import org.stilab.metrics.counter.interfaces.BlockTypeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.BlockTree;

public class ProviderCounter implements BlockTypeCounter {

  @Override
  public int blockTypeCounter(Tree tree) {
    BlockCheckerType blockCheckerType = new BlockCheckerTypeImpl();
    return (int) tree.children().stream()
      .filter(BlockTree.class::isInstance)
      .filter(child -> blockCheckerType.isProvider((BlockTree) child))
      .count();
  }
}

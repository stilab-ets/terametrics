package org.stilab.metrics.counter.block.counter;

import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.interfaces.BlockCheckerType;
import org.stilab.interfaces.BlockTypeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.BlockTree;

public class VariableCounter implements BlockTypeCounter {

  @Override
  public int blockTypeCounter(Tree tree) {
    BlockCheckerType blockCheckerType = new BlockCheckerTypeImpl();
    return (int) tree.children().stream()
      .filter(child -> child instanceof BlockTree)
      .filter(child -> blockCheckerType.isVariable((BlockTree) child))
      .count();
  }

}

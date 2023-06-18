package org.stilab.metrics.counter.block.finder;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.List;
import java.util.stream.Collectors;

public class TopBlockFinder {


  public List<BlockTreeImpl> findTopBlock(Tree root) {

    return root.children().stream()
      .filter(node -> node instanceof BlockTreeImpl)
      .map(node -> (BlockTreeImpl) node)
      .collect(Collectors.toList());

  }


}

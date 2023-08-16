package org.stilab.metrics.counter.block.finder;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BodyTreeImpl;
import java.util.Optional;

public class BodyTreeFinder {

  public Tree find(Tree blockTree) {
    Optional<BodyTreeImpl> bodyTreeOptional = blockTree.children().stream()
      .filter(tree1 -> tree1 instanceof BodyTreeImpl)
      .map(tree1 -> (BodyTreeImpl) tree1)
      .findFirst();
    BodyTreeImpl bodyTree = bodyTreeOptional.orElse(null);
    return bodyTree;
  }
}

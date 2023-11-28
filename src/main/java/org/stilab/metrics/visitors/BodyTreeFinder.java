package org.stilab.metrics.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BodyTreeImpl;
import java.util.Optional;

public class BodyTreeFinder {

  public Tree find(Tree blockTree) {
    Optional<BodyTreeImpl> bodyTreeOptional = blockTree.children().stream()
      .filter(BodyTreeImpl.class::isInstance)
      .map(BodyTreeImpl.class::cast)
      .findFirst();
    return bodyTreeOptional.orElse(null);
  }
}

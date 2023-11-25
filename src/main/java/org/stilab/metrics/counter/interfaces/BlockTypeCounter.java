package org.stilab.metrics.counter.interfaces;

import org.sonar.iac.common.api.tree.Tree;
public interface BlockTypeCounter {
  public int blockTypeCounter(Tree tree);
}

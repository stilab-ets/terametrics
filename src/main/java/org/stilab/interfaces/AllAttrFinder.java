package org.stilab.interfaces;

import org.sonar.iac.common.api.tree.Tree;
import java.util.List;

public interface AllAttrFinder {

  public List<Tree> getAllAttrs(Tree root);
}

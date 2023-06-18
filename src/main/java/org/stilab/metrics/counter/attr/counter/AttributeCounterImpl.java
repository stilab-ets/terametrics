package org.stilab.metrics.counter.attr.counter;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.interfaces.AttributeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import java.util.List;

public class AttributeCounterImpl implements AttributeCounter {


  @Override
//  TerraformTree tree
  public int countAttribute(Tree tree) {
//    return (int) tree.children().stream()
//      .filter(child -> child instanceof AttributeTree)
//      .count();
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes((BlockTreeImpl) tree);
    return attributeTrees.size();
  }
}

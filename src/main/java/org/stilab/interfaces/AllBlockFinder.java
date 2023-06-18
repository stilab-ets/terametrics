package org.stilab.interfaces;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.List;

public interface AllBlockFinder {

  public List<BlockTreeImpl> getAllBlock(Tree root);


//  public List<BlockTreeImpl> getTopBlocks(Tree root);

}

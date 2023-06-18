package org.stilab.interfaces;

import org.sonar.iac.terraform.api.tree.BlockTree;

public interface BlockCheckerType {

  public boolean isResource(BlockTree tree);

  public boolean isProvider(BlockTree tree);

  public boolean isOutput(BlockTree tree);

  public boolean isData(BlockTree tree);

  public boolean isModule(BlockTree tree);

  public boolean isLocals(BlockTree tree);

  public boolean isVariable(BlockTree tree);

}

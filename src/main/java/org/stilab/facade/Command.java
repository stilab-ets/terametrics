package org.stilab.facade;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Command {
  void execute(String filePath, String target);
}

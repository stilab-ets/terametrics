package org.stilab.parser.facade;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Command {
  void execute(String filePath, String target) throws Exception;
}

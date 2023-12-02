package org.stilab.parser.facade;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Command {
  void execute() throws Exception;
}

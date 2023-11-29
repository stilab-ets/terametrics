package org.stilab.parser.facade;

import org.stilab.parser.granularity.dir.LocalAnalyzer;
import org.stilab.parser.granularity.dir.RepoAnalyzer;

public class DirCommand implements Command{

  private String repo_full_name;

  public DirCommand(String repo_full_name){
      this.repo_full_name = repo_full_name;
  }

  @Override
  public void execute(String filePath, String target) {
    RepoAnalyzer distantAnalyzer = new LocalAnalyzer(repo_full_name, filePath, target);
    distantAnalyzer.analyzeTfFiles();
  }
}

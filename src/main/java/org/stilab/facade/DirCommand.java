package org.stilab.facade;

import org.stilab.metrics.granularity.dir.LocalAnalyzer;
import org.stilab.metrics.granularity.dir.RepoAnalyzer;

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

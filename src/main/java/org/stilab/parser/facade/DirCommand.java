package org.stilab.parser.facade;

import org.stilab.parser.granularity.dir.LocalAnalyzer;
import org.stilab.parser.granularity.dir.RepoAnalyzer;

public class DirCommand implements Command{

    private String projectName;

    public DirCommand(String projectName){
      this.projectName = projectName;
    }

    @Override
    public void execute(String filePath, String target) {
      RepoAnalyzer distantAnalyzer = new LocalAnalyzer(this.projectName, filePath, target);
      distantAnalyzer.analyzeTfFiles();
    }
}

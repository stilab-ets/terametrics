package org.stilab.parser.facade;

import org.stilab.parser.granularity.dir.LocalAnalyzer;
import org.stilab.parser.granularity.dir.RepoAnalyzer;

public class DirCommand implements Command{

    private String projectDirectory;
    private String projectName;

    private String target;

    public DirCommand(String projectName, String projectDirectory, String target){
      this.projectDirectory = projectDirectory;
      this.projectName = projectName;
      this.target = target;
    }

    @Override
    public void execute() {
      RepoAnalyzer distantAnalyzer = new LocalAnalyzer(this.projectName, this.projectDirectory, target);
      distantAnalyzer.analyzeTfFiles();
    }
}

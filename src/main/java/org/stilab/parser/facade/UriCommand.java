package org.stilab.parser.facade;

import org.stilab.parser.granularity.dir.DistantAnalyzer;
import org.stilab.parser.granularity.dir.RepoAnalyzer;

public class UriCommand implements Command{

    private String full_name_repo;

    public UriCommand(String full_name_repo) {
      this.full_name_repo = full_name_repo;
    }

    @Override
    public void execute(String filePath, String target) throws Exception {
      RepoAnalyzer distantAnalyzer = new DistantAnalyzer(this.full_name_repo, filePath, target);
      distantAnalyzer.analyzeTfFiles();
    }


}

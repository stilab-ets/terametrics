package org.stilab.parser.facade;

import org.stilab.parser.granularity.dir.DistantAnalyzer;
import org.stilab.parser.granularity.dir.RepoAnalyzer;

public class UriCommand implements Command{

    private String full_name_repo;
    private String localStore;
    private String target;

    public UriCommand(String full_name_repo, String localStore, String target) {
      this.full_name_repo = full_name_repo;
      this.localStore = localStore;
      this.target = target;
    }

    @Override
    public void execute() throws Exception {
      RepoAnalyzer distantAnalyzer = new DistantAnalyzer(this.full_name_repo, this.localStore, this.target);
      distantAnalyzer.analyzeTfFiles();
    }


}

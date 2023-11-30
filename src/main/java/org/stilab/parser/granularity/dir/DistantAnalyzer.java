package org.stilab.parser.granularity.dir;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.List;

public class DistantAnalyzer extends RepoAnalyzer{

    private String REPO_URL;
    private Git git;

    public DistantAnalyzer(String full_name_repo, String absoluteLocalPath, String targetFile) throws GitAPIException {
      super(full_name_repo, absoluteLocalPath, targetFile);

      this.REPO_URL = "https://github.com/" + full_name_repo + ".git";
      dirAnalyzerService.deleteDirectory(new File(this.LOCAL_PATH));
      this.git = downloadRepo();

    }

    private Git downloadRepo() throws GitAPIException {
      // Clone Decorator
      return Git.cloneRepository()
        .setURI(this.REPO_URL)
        .setDirectory(new File(this.LOCAL_PATH)).call();
    }

    public void tearDown() {
      // Clean up resources after the test
      if ( (this.git != null)) {
        this.git.close();
      }
    }

    public List<String> getTfFilesList() {
      File repositoryDirectory = this.git.getRepository().getDirectory();
      File parentDirectory = repositoryDirectory.getParentFile();
      return dirAnalyzerService.extractTfFiles(new File(parentDirectory, ""));
    }

}

package org.stilab.metrics.granularity.dir;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.List;

public class LocalAnalyzer extends RepoAnalyzer{

  public LocalAnalyzer(String full_name_repo, String absoluteLocalPath, String targetFile){
    super(full_name_repo, absoluteLocalPath, targetFile);
  }

  protected List<String> getTfFilesList() {
    File file = new File(LOCAL_PATH, "");
    return dirAnalyzerService.extractTfFiles(file);
  }

}

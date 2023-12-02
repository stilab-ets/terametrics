package org.stilab.parser.granularity.dir;

import java.io.File;
import java.util.List;

public class LocalAnalyzer extends RepoAnalyzer{

  public LocalAnalyzer(String full_name_repo, String absoluteLocalPath, String targetFile){
    super(full_name_repo, absoluteLocalPath, targetFile);
    System.out.println(this.LOCAL_PATH);
  }

  protected List<String> getTfFilesList() {
    File file = new File(LOCAL_PATH, "");
    return dirAnalyzerService.extractTfFiles(file);
  }

}

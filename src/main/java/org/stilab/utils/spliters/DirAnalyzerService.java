package org.stilab.utils.spliters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirAnalyzerService {


  public void deleteDirectory(File directory) {
    if (directory.exists()) {

      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            deleteDirectory(file);
          } else {
            file.delete();
          }
        }
      }
      directory.delete();
    }
  }

  public List<String> extractTfFiles(File directory) {
    List<String> tfFiles = new ArrayList<>();
    extractTfFilesRecursively(directory, tfFiles);
    return tfFiles;
  }

  public void extractTfFilesRecursively(File directory, List<String> tfFiles) {
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          extractTfFilesRecursively(file, tfFiles);
        } else {
          // Check if the file has a '.tf' extension
          if (file.getName().endsWith(".tf")) {
            tfFiles.add(file.getAbsolutePath());
          }
        }
      }
    }
  }


}

package org.stilab.block;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.MetricsCalculatorBlocks;
import org.stilab.utils.mapper.BlockPosition;
import org.stilab.utils.spliters.BlockDivider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepoAnalyzer {

      private String REPO_URL;
      private String LOCAL_PATH;
      private String TARGET_FILE;
      private boolean cloneFromGitHub;
      private Git git;

      public RepoAnalyzer(String githubUrl, String localPath, String targetFile, boolean cloneFromGitHub) throws GitAPIException {
              this.TARGET_FILE = targetFile;
              this.REPO_URL = githubUrl;
              this.LOCAL_PATH = localPath;
              this.cloneFromGitHub = cloneFromGitHub;

              if (this.cloneFromGitHub) {
                // Extract project name from the repository URL
                String projectName = extractProjectName(REPO_URL);
                // Set up the local path with the project name
                String localPathWithProject = LOCAL_PATH + projectName;
                // Delete the local directory before cloning (if it already exists)
                deleteDirectory(new File(localPathWithProject));
                // Clone Repository
                CloneCommand cloneCommand = Git.cloneRepository()
                  .setURI(this.REPO_URL)
                  .setDirectory(new File(localPathWithProject));

                this.git = cloneCommand.call();
              }
      }

      public void tearDown() {
        // Clean up resources after the test
        if ( (this.cloneFromGitHub) & (this.git != null)) {
          this.git.close();
        }
      }

      private String extractProjectName(String repoURL) {
        // Extracts "terraform-aws-eks" from the URL
        Pattern pattern = Pattern.compile(".*/(.*?)(?:\\.git)?$");
        Matcher matcher = pattern.matcher(repoURL);

        if (matcher.find()) {
          return matcher.group(1);
        } else {
          throw new IllegalArgumentException("Invalid repository URL");
        }
      }

      private void deleteDirectory(File directory) {
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

      private List<String> extractTfFiles(File directory) {
        List<String> tfFiles = new ArrayList<>();
        extractTfFilesRecursively(directory, tfFiles);
        return tfFiles;
      }

      private void extractTfFilesRecursively(File directory, List<String> tfFiles) {
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

      private List<String> getTfFilesList() {
        if (this.cloneFromGitHub) {
          File repositoryDirectory = this.git.getRepository().getDirectory();
          File parentDirectory = repositoryDirectory.getParentFile();
          return extractTfFiles(new File(parentDirectory, ""));
        } else {
          // Use the provided local path when not cloning from GitHub
          return extractTfFiles(new File(this.LOCAL_PATH));
        }
      }

      private JSONObject createJsonObject(List<String> tfFiles) {
        JSONObject root = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (String tfFile : tfFiles) {
          BlockDivider blockDivider = new BlockDivider(tfFile);
          List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = blockDivider.divideFilePerBlock();
          MetricsCalculatorBlocks metricsCalculatorBlocks = new MetricsCalculatorBlocks(blockPositions);
          List<JSONObject> objects = metricsCalculatorBlocks.measureMetricsPerBlocks();

          JSONObject object = new JSONObject();
          object.put("file", tfFile);
          object.put("metrics", objects);
          jsonArray.add(object);
        }

        root.put("project", this.REPO_URL);
        root.put("data", jsonArray);

        return root;
      }

      private void saveJsonObjectToFile(JSONObject jsonObject) {
        try (FileWriter fileWriter = new FileWriter(this.TARGET_FILE)) {
          fileWriter.write(jsonObject.toString());
          System.out.println("JSON objects saved to file: " + this.TARGET_FILE);
        } catch (IOException e) {
          System.err.println("Error while saving JSON objects to file: " + e.getMessage());
        }
      }

      public int analyzeTfFiles() {
        List<String> tfFiles = getTfFilesList();
        JSONObject jsonObject = createJsonObject(tfFiles);
        saveJsonObjectToFile(jsonObject);

        tearDown();
        return tfFiles.size();
      }

}

package org.stilab.metrics;

import junit.framework.TestCase;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.stilab.granularity.dir.DistantAnalyzer;
import org.stilab.granularity.dir.LocalAnalyzer;
import org.stilab.granularity.dir.RepoAnalyzer;


public class DirAnalysisTest extends TestCase {

    private static final String REPO_FULL_NAME = "terraform-aws-modules/terraform-aws-eks";
    private static final String LOCAL_PATH = "src/test/java/org/stilab/block/data/";
    private static final String TARGET_FILE = "src/test/java/org/stilab/block/data/directory_metrics.json";

    @Test
    public void testDirDistantAnalysis() throws GitAPIException {
      RepoAnalyzer repoAnalyzer = new DistantAnalyzer(REPO_FULL_NAME, LOCAL_PATH, TARGET_FILE);
      JSONObject jsonObject = repoAnalyzer.analyzeTfFiles();
      assertEquals("terraform-aws-modules/terraform-aws-eks", (String) jsonObject.get("project"));
    }

    @Test
    public void testDirLocalAnalysis(){

      String local_path = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\data\\terraform-aws-modules\\terraform-aws-eks";
      String target = "src/test/java/org/stilab/block/data/local_metrics.json";

      LocalAnalyzer repoAnalyzer = new LocalAnalyzer(REPO_FULL_NAME, local_path, target);
      JSONObject jsonObject = repoAnalyzer.analyzeTfFiles();
      assertEquals("terraform-aws-modules/terraform-aws-eks", (String) jsonObject.get("project"));
    }



}

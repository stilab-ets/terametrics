package org.stilab.block;

import junit.framework.TestCase;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;


public class DirAnalysisTest extends TestCase {

    private static final String REPO_URL = "https://github.com/terraform-aws-modules/terraform-aws-eks.git";
    private static final String LOCAL_PATH = "src/test/java/org/stilab/block/data/";
    private static final String TARGET_FILE = "src/test/java/org/stilab/block/data/positions.json";

    @Test
    public void testDirAnalysis() throws GitAPIException {
      RepoAnalyzer repoAnalyzer = new RepoAnalyzer(REPO_URL, LOCAL_PATH, TARGET_FILE, true);
      assertEquals(57, repoAnalyzer.analyzeTfFiles());

    }





}

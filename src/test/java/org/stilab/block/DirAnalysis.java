package org.stilab.block;

import junit.framework.TestCase;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DirAnalysis extends TestCase {

    private static final String REPO_URL = "https://github.com/terraform-aws-modules/terraform-aws-eks.git";
    private static final String LOCAL_PATH = "src/test/java/org/stilab/block/data/";
    private static final String TARGET_FILE = "src/test/java/org/stilab/block/data/positions.json";

    @Test
    public void testDirAnalysis() throws GitAPIException {
      RepoAnalyzer repoAnalyzer = new RepoAnalyzer(REPO_URL, LOCAL_PATH, TARGET_FILE, true);
      assertEquals(57, repoAnalyzer.analyzeTfFiles());

    }





}

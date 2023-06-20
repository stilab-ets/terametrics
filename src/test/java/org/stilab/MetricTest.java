package org.stilab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.BlockLabelIdentifier;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.utils.BlockServiceLocator;
import org.stilab.utils.MetricsCalculator;
import org.stilab.utils.Pair;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MetricTest  extends TestCase {


  public String fileContent = "resource \"aws_instance\" \"web\" {\n" +
    "  # ...\n" +
    "\n" +
    "  provisioner \"local-exec\" {\n" +
    "    command = \"echo ${self.private_ip} >> private_ips.txt\"\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "\n" +
    "resource \"terraform_data\" \"example1\" {\n" +
    "  provisioner \"local-exec\" {\n" +
    "    command = \"open WFH, '>completed.txt' and print WFH scalar localtime\"\n" +
    "    interpreter = [\"perl\", \"-e\"]\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "resource \"terraform_data\" \"example2\" {\n" +
    "  provisioner \"local-exec\" {\n" +
    "    command = \"Get-Date > completed.txt\"\n" +
    "    interpreter = [\"PowerShell\", \"-Command\"]\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "resource \"aws_instance\" \"web\" {\n" +
    "  # ...\n" +
    "\n" +
    "  provisioner \"local-exec\" {\n" +
    "    command = \"echo $FOO $BAR $BAZ >> env_vars.txt\"\n" +
    "\n" +
    "    environment = {\n" +
    "      FOO = \"bar\"\n" +
    "      BAR = 1\n" +
    "      BAZ = \"true\"\n" +
    "    }\n" +
    "  }\n" +
    "}\n";

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public MetricTest( String testName )
  {
    super( testName );
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite()
  {
    return new TestSuite( MetricTest.class );
  }

  /**
   * Rigourous Test :-)
   */
  public void testGettingLabelsOfBlock() {

    HclParser hclParser = new HclParser();

    Tree tree = hclParser.parse(fileContent);

    // To look for the Parsed One
    TopBlockFinder topBlockFinder = new TopBlockFinder();

    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    BlockTreeImpl topBlock = blocks.get(0);

    BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();

    List<String> expected = Arrays.asList("aws_instance", "web");

    assertEquals(expected, blockLabelIdentifier.identifyLabelsOfBlock(topBlock));
  }

  public void testBlockIdentifier() {

    HclParser hclParser = new HclParser();
    Tree tree = hclParser.parse(fileContent);

    // to look for the Parsed One
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blockTrees = topBlockFinder.findTopBlock(tree);
    BlockTreeImpl topBlock = blockTrees.get(0);

    BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
    List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(topBlock);

    labels.add(0, topBlock.key().value());
    List<String> expected = Arrays.asList("resource", "aws_instance", "web");
    assertEquals(expected, this.concatElementsOfList(labels));
  }

  public String concatElementsOfList(List<String> stringList) {
    return String.join(" ", stringList);
  }

  public void testMetrics() {

    String pathToFile = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tmp_blob.tf";
    int impactedLineIndex = 187;

    BlockServiceLocator blockServiceLocator = new BlockServiceLocator();
    Pair<BlockTreeImpl, String> identifiedBlockPair = blockServiceLocator.identifyRightBlock(pathToFile,
      impactedLineIndex);


    MetricsCalculator metricsCalculator = new MetricsCalculator();
    ;

    System.out.println(
      metricsCalculator.measureMetrics(identifiedBlockPair.getFirst(), identifiedBlockPair.getSecond())
    );
  }

}

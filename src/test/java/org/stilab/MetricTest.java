package org.stilab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.BlockLabelIdentifier;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;

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


}

package org.stilab.other;

import junit.framework.TestCase;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block_level.TokenIdentifier;

import java.util.List;

public class BlockReBuilderTest extends TestCase {

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

  public void testRebuildingOfABlock() {


    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(fileContent);

    // GET THE TOP BLOCK
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    BlockTreeImpl firstBlock = blocks.get(0);

    TokenIdentifier tokenIdentifier = new TokenIdentifier();



  }

}

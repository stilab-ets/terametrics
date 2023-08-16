package org.stilab.other;

import junit.framework.TestCase;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block_level.VariablesIdentifier;

import java.io.File;

public class VariableExperTest extends TestCase {

  public void testVarIdentification() {
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\vars.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);
    VariablesIdentifier variablesIdentifier = new VariablesIdentifier();
    variablesIdentifier.filterVarsFromBlock(blockTree);
//    assertEquals(variablesIdentifier.countVars(), 3);

  }
}

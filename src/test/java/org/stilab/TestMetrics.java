package org.stilab;

import junit.framework.Test;
import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block.size.BlockComplexity;
import org.stilab.metrics.counter.expression.ReferenceIdentifier;
import org.stilab.utils.BlockDivider;
import org.stilab.utils.BlockPosition;
import org.stilab.utils.MetricsCalculatorBlocks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestMetrics extends TestCase {


  public void testDividingBlock() {

    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
      "\\terraform_miner\\src\\test\\java\\org\\stilab\\test.tf");

    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(file);

    // GET THE TOP BLOCK
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    BlockTreeImpl blockTree = blocks.get(0);

    AttrFinderImpl attrFinder = new AttrFinderImpl();

    AttributeTreeImpl attr2 = attrFinder.getAttributes(blockTree).get(1);

    System.out.println( ((SyntaxTokenImpl) attr2.value().children().get(0)).value() );

    assertEquals(3, 4);

  }


  public void testNLocMetric() {

    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\test.tf";

    File file = new File(filePath);

    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(file);

    // GET THE TOP BLOCK
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    BlockTreeImpl blockTree = blocks.get(0);

    BlockComplexity blockComplexity = new BlockComplexity(filePath, blockTree);

    assertEquals(7, blockComplexity.NLOC());

  }

  public void testNumReferences() {

    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\test.tf";

    File file = new File(filePath);

    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(file);

    // GET THE TOP BLOCK
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    BlockTreeImpl blockTree = blocks.get(0);

    ReferenceIdentifier referenceIdentifier = new ReferenceIdentifier();

    assertEquals(3, referenceIdentifier.filterAttributeAccessFromBlock(blockTree).size());


  }



}

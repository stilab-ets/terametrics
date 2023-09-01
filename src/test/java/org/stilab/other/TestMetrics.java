package org.stilab.other;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block_level.BlockComplexity;
import org.stilab.metrics.counter.block_level.*;
import org.stilab.utils.BlockPosition;
import org.stilab.utils.ServiceLocator;

import java.io.File;
import java.util.List;
import java.util.Map;

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

    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\test2.tf";

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

    assertEquals(27, blockComplexity.NLOC());

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

  public void testNumLiteralExpression() {
    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\test2.tf";
    File file = new File(filePath);
    // Call Hcl Parser
    HclParser hclParser = new HclParser();
    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(file);
    // GET THE TOP BLOCK
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
    BlockTreeImpl blockTree = blocks.get(0);

//    System.out.println(
//      ((ObjectElementTreeImpl) ((ObjectTreeImpl) blockTree.value().statements().get(4).value()).properties().get(0)).key()
//    );
    LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
    List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(blockTree);

    for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
      System.out.println(literalExprTree.value());
    }

    assertEquals(254, literalExpressionIdentifier.filterLiteralExprFromBlock(blockTree).size());
  }

  public void testNumTupleUsage() {
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
    TupleIdentifier tupleIdentifier = new TupleIdentifier();
    assertEquals(1, tupleIdentifier.filterTuplesFromBlock(blockTree).size());
  }

  public void testTextEntropy() {
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
//    LiteralExprTreeImpl literalExprTree = (LiteralExprTreeImpl) blockTree.value().statements().get(1).value();


    TokenIdentifier tokenIdentifier = new TokenIdentifier();
    TextEntropy textEntropy = new TextEntropy();
    List<Character> characters = tokenIdentifier
      .textualize(blockTree);
    Map<Character, Integer> characterFrequency = textEntropy
      .countCharacterFrequency(characters);
    double textEntropyMeasure = textEntropy
      .textEntropy(characterFrequency);

    assertEquals(5.78, textEntropyMeasure);
  }

  public void testServiceLocator() {


    String labelIdentifierExpected = "data aws_iam_policy_document irsa_assume_role";
    int startExpected = 22 ;
    int endExpected = 47;
    BlockPosition actualBlockPosition = null;


    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tmp_blob.tf";

    ServiceLocator serviceLocator = new ServiceLocator(labelIdentifierExpected,
      filePath);

    // Block Position
    List<BlockPosition> blockPositions = serviceLocator.getRightBlocks();

    // save Identifiers
    List<JSONObject> positionsAsJson = serviceLocator.saveIdentifiedBlocks();


    assertEquals(startExpected, positionsAsJson.get(0).get("start"));
    assertEquals(endExpected, positionsAsJson.get(0).get("end"));

  }

  public void testSavingBlockLocation() {

    String blockIdentifier = "data aws_iam_policy_document irsa_assume_role";
    String filePath = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tmp_blob.tf";
    String target = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block_locations.json";
    int startExpected = 22 ;
    int endExpected = 47;
    BlockPosition actualBlockPosition = null;

    ServiceLocator serviceLocator = new ServiceLocator(blockIdentifier, filePath, target);
    // Save the positions as json objects
    List<JSONObject> objects = serviceLocator.saveIdentifiedBlocks();
    // Put them to JSON file
    serviceLocator.saveJsonToFile(objects);

  }

  public void testSavingAllBlockPosition() {
    String file = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tmp_blob.tf";
    String target = "C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block_locations_1.json";

    ServiceLocator serviceLocator = new ServiceLocator(file, target);
    // Save the positions as json objects
    List<JSONObject> objects = serviceLocator.obtainAllBlockPosition();
    // Put them as JSON file
    serviceLocator.saveJsonToFile(objects);
  }







}

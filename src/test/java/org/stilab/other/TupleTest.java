package org.stilab.other;

import junit.framework.TestCase;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block_level.*;

import java.io.File;
import java.util.List;

public class TupleTest extends TestCase {

    public void testIdentifyingTuples() {

      HclParser hclParser = new HclParser();

      File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\tuple_test.tf");

      Tree tree = hclParser.parse(file);

      BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

      IndexAccessIdentifier indexAccessIdentifier = new IndexAccessIdentifier();

      assertEquals(2, indexAccessIdentifier.identifyIndexAccessFromBlock(blockTree).size());

    }

    public void testCompexityMeasure() {

      HclParser hclParser = new HclParser();

      File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\condition_test.tf");

      Tree tree = hclParser.parse(file);

      BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

      MccabeCC maccabeCC = new MccabeCC();

      assertEquals(maccabeCC.sumMccabeCC(blockTree), 4.0);
    }

    public void testLiteralExpressionMeasure() {
      HclParser hclParser = new HclParser();
      File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\literal_expressions.tf");
      Tree tree = hclParser.parse(file);
      BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(2);

      LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();

      assertEquals(literalExpressionIdentifier.filterLiteralExprFromBlock(blockTree).size(), 7);
    }

  public void testNumDecisionMeasure() {
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\literal_expressions.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(2);

    DecisionsIdentifier decisionsIdentifier = new DecisionsIdentifier();

    assertEquals(decisionsIdentifier.identifyDecisionsOperators(blockTree).size(), 4);
  }

  public void testInDepthOfComplexityMeasure(){
      HclParser hclParser = new HclParser();
      File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\complex_local.tf");
      Tree tree = hclParser.parse(file);
      BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

      LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
      MccabeCC mccabeCC = new MccabeCC();
      TupleIdentifier tupleIdentifier = new TupleIdentifier();
      AttrFinderImpl attrFinder = new AttrFinderImpl();

      assertEquals(literalExpressionIdentifier.filterLiteralExprFromBlock(blockTree).size(), 0);
      assertEquals(mccabeCC.sumMccabeCC(blockTree), 5.0);
    }

  public void testTupleCounts(){
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
      "\\terraform_miner\\src\\test\\java\\org\\stilab\\complex_local.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

    TupleIdentifier tupleIdentifier = new TupleIdentifier();

    List<TerraformTreeImpl> tuples = tupleIdentifier.filterTuplesFromBlock(blockTree);

    int numTuples = tupleIdentifier.totalNumberOfTuples();
    double avgTuples = tupleIdentifier.avgNumberOfTuples();
    int maxTuples = tupleIdentifier.maxNumberOfTuples();

    // Summary of Number of tuple per
    assertEquals(numTuples, 3);
    assertEquals(avgTuples, 1.0);
    assertEquals(maxTuples, 1);

  }

  public void testObjectWrapperCounts() {

//   TODO: Add the Tuple Object Case.
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
      "\\terraform_miner\\src\\test\\java\\org\\stilab\\complex_local.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(1);

    ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();

    List<TerraformTreeImpl> objects = objectWrapperIdentifier.filterObjectsFromBlock(blockTree);

    int numObjects = objectWrapperIdentifier.totalNumberOfObjects();
    double avgObjects = objectWrapperIdentifier.avgNumberOfObjects();
    int maxObjects = objectWrapperIdentifier.maxNumberOfObjects();

    // Summary of Number of objects per
    assertEquals(numObjects, 3);
    assertEquals(avgObjects, 1.5);
    assertEquals(maxObjects, 2);
  }

  public void testTupleElements(){
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
      "\\terraform_miner\\src\\test\\java\\org\\stilab\\complex_local.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

    TupleIdentifier tupleIdentifier = new TupleIdentifier();

    List<TerraformTreeImpl> tuples = tupleIdentifier.filterTuplesFromBlock(blockTree);
    TupleElementsIdentifier tupleElementsIdentifier = new TupleElementsIdentifier(tuples);

    int numElemTuples = tupleElementsIdentifier.getTotalNumberOfElementsOfDifferentTuples();
    double avgElemTuples = tupleElementsIdentifier.avgNumberOfElementsPerDifferentTuples();
    int maxElemTuples = tupleElementsIdentifier.maxNumberOfElementsPerDifferentTuples();

    assertEquals(numElemTuples, 3);
    assertEquals(avgElemTuples, 1.0);
    assertEquals(maxElemTuples, 1);

  }
//
  public void testObjectWrapperElements() {

      HclParser hclParser = new HclParser();
      File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
        "\\terraform_miner\\src\\test\\java\\org\\stilab\\complex_local.tf");
      Tree tree = hclParser.parse(file);
      BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(1);

      ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();

      List<TerraformTreeImpl> objects = objectWrapperIdentifier.filterObjectsFromBlock(blockTree);
      ObjectWrapperElementIdentifier objectWrapperElementIdentifier = new ObjectWrapperElementIdentifier(objects);

      int numElementObjects = objectWrapperElementIdentifier.getTotalNumberOfElementsOfDifferentObjects();
      double avgElementObjects = objectWrapperElementIdentifier.avgNumberOfElementsPerDifferentObjects();
      int maxElementObjects = objectWrapperElementIdentifier.maxNumberOfElementsPerDifferentObjects();

      // Summary of Number of objects per
      assertEquals(numElementObjects, 3);
      assertEquals(avgElementObjects, 1.5);
      assertEquals(maxElementObjects, 2);
  }

}

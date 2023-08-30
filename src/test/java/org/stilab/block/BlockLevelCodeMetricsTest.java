package org.stilab.block;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block.size.BlockComplexity;
import org.stilab.metrics.counter.block_level.*;
import java.io.File;
import java.util.List;

public class BlockLevelCodeMetricsTest extends TestCase {

   private BlockTreeImpl identifiedBlock;
   private JSONObject metrics;

  @Override
  protected void setUp() throws Exception {

    super.setUp();

    File base = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\base.tf");
    HclParser hclParser = new HclParser();
    Tree tree = hclParser.parse(base);

    // To look for the Parsed One
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
    identifiedBlock = blocks.get(0);
    metrics = new JSONObject();
  }

  public void testBlockMetaInfoIdentification() {
    BlockMetaInfo metaInfo = new BlockMetaInfo();
    metrics = metaInfo.updateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("impacted_block_type"), "aws_elastic_beanstalk_environment tfenvtest");
    assertEquals(metrics.get("block"), "resource");
    assertEquals(metrics.get("start_block"), 1);
    assertEquals(metrics.get("end_block"), 156);
    assertEquals(metrics.get("block_identifiers"), "resource aws_elastic_beanstalk_environment tfenvtest");
  }

  public void testComparisonOperatorsIdentification(){
    ComparisonOperatorsIdentifier cmp = new ComparisonOperatorsIdentifier();
    metrics = cmp.updateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("numComparisonOperators"), 1);
    assertEquals(metrics.get("avgComparisonOperators"), 0.05);
    assertEquals(metrics.get("maxComparisonOperators"), 1);
  }

  public void testConditionalExpressionIdentification(){
//    TODO: review this later before reviewing how to measure the complexity of a Block
    ConditionalExpressionIdentifier conditionalExpressionIdentifier = new ConditionalExpressionIdentifier();
    metrics = conditionalExpressionIdentifier.updateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("numConditions"), 2);
    assertEquals(metrics.get("avgConditions"), 0.09);
    assertEquals(metrics.get("maxConditions"), 1);
  }

    public void testLogicalOperatorsIdentification(){
      LogicalOperationsIdentifier logOper = new LogicalOperationsIdentifier();
      metrics = logOper.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numLogiOpers"), 5);
      assertEquals(metrics.get("avgLogiOpers"), 0.23);
      assertEquals(metrics.get("maxLogiOpers"), 4);
    }

    public void testDynamicBlocksIdentification(){
      DynamicBlocksIdentifier dynamicBlocksIdentifier = new DynamicBlocksIdentifier();
      metrics = dynamicBlocksIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDynamicBlocks"), 1);
    }

    public void testLookUpFunctionCallIdentification() {
      FunctionCallExpressionIdentifier functionCallExpressionIdentifier = new FunctionCallExpressionIdentifier();
      LookUpFunctionIdentifier lookUpFunctionIdentifier = new LookUpFunctionIdentifier(functionCallExpressionIdentifier);
      metrics = lookUpFunctionIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLookUpFunctionCall"), 0);
    }

    public void testNestedBlocksIdentification(){
      NestedBlockIdentifier nestedBlockIdentifier = new NestedBlockIdentifier();
      metrics = nestedBlockIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numNestedBlocks"), 3);
      assertEquals(metrics.get("avgDepthNestedBlocks"), 16.33);
      assertEquals(metrics.get("maxDepthNestedBlocks"), 36);
      assertEquals(metrics.get("minDepthNestedBlocks"), 5);
    }

    public void testFuncCallIdentification(){
      FunctionCallExpressionIdentifier functionCallExpressionIdentifier
        = new FunctionCallExpressionIdentifier();
      metrics = functionCallExpressionIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numFunctionCall"), 9);
      assertEquals(metrics.get("avgFunctionCall"), 0.41);
      assertEquals(metrics.get("maxFunctionCall"), 2);
    }

    public void testParametersIdentification(){
      FunctionParametersIdentifier functionParametersIdentifier = new FunctionParametersIdentifier();
      metrics = functionParametersIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numParams"), 17);
      assertEquals(metrics.get("avgParams"), 1.89);
      assertEquals(metrics.get("maxParams"), 3);

    }

    public void testHereDocIdentification(){
      HereDocIdentifier hereDocIdentifier = new HereDocIdentifier();
      metrics = hereDocIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numHereDocs"), 4);
      assertEquals(metrics.get("avgHereDocs"), 0.18);
//      assertEquals(metrics.get("maxHereDocs"), 1);
//      We take into account just the lines between EOF delimiter:
//               <<-EOF
//               lines ....
//               ...
//               EOF
      assertEquals(metrics.get("numLinesHereDocs"), 89);
      assertEquals(metrics.get("avgLinesHereDocs"), 22.25);
      assertEquals(metrics.get("maxLinesHereDocs"), 32);

    }

    public void testIndexAccessIdentification(){
        IndexAccessIdentifier indexAccessIdentifier = new IndexAccessIdentifier();
        metrics = indexAccessIdentifier.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numIndexAccess"), 2);
        assertEquals(metrics.get("avgIndexAccess"), 0.09);
        assertEquals(metrics.get("maxIndexAccess"), 1);
    }

    public void testLiteralExpressionIdentification(){
      LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
      metrics = literalExpressionIdentifier.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLiteralExpression"), 29);
      //  Number of String values means number of hard coded things
      assertEquals(metrics.get("numStringValues"), 16);
      //  SUM of Length of literal Expressions (in term Of tokens)
      assertEquals(metrics.get("sumLengthStringValues"), 3896);
      //  Average Length Of Literal Expressions (in term of Tokens)
      assertEquals(metrics.get("avgLengthStringValues"), 243.5);
      //  MAX Length Of Literal Expressions (in term of tokens)
      assertEquals(metrics.get("maxLengthStringValues"), 1717);

    }

    public void testLoopsExpressionIdentification() {
        LoopsExpressionIdentifier loopsExpressionIdentifier = new LoopsExpressionIdentifier();
        metrics = loopsExpressionIdentifier.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numLoops"), 3);
        assertEquals(metrics.get("avgLoops"), 0.14);
        assertEquals(metrics.get("maxLoops"), 2);
    }

    public void testMathOperatorsIdentification(){
      MathOperations mathOperations = new MathOperations();
      metrics = mathOperations.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numMathOperations"), 2);
      assertEquals(metrics.get("avgMathOperations"), 0.09);
      assertEquals(metrics.get("maxMathOperations"), 2);

    }

    public void testMCCabCCIdentification() {
        MccabeCC mccabeCC = new MccabeCC();
        metrics = mccabeCC.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("avgMccabeCC"), 1.23);
        assertEquals(metrics.get("sumMccabeCC"), 27);
        assertEquals(metrics.get("maxMccabeCC"), 3);
    }

    public void testMetaArgumentsIdentification() {
        MetaArgumentIdentifier metaArgumentIdentifier = new MetaArgumentIdentifier();
        metrics = metaArgumentIdentifier.updateMetrics(metrics, identifiedBlock);
        assertEquals(metrics.get("numMetaArg"), 2);
    }

    public void testObjectWrapperIdentification() {
      ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();
      metrics = objectWrapperIdentifier.updateMetric(metrics, identifiedBlock);
       assertEquals(metrics.get("numObjects"), 5);
       assertEquals(metrics.get("avgObjects"), 0.23);
       assertEquals(metrics.get("maxObjects"), 2);
    }

    public void testObjectWrapperElementsIdentification(){
      ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();
      List<TerraformTreeImpl> objects = objectWrapperIdentifier.filterObjectsFromBlock(identifiedBlock);
      ObjectWrapperElementIdentifier objectWrapperElementIdentifier = new ObjectWrapperElementIdentifier(objects);
      metrics = objectWrapperElementIdentifier.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numElemObjects"), 7);
      assertEquals(metrics.get("avgElemObjects"), 1.4);
      assertEquals(metrics.get("maxElemObjects"), 2);

    }

    public void testReferenceIdentificationIdentification(){
        ReferenceIdentifier referenceIdentifier = new ReferenceIdentifier();
        metrics = referenceIdentifier.updateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numReferences"), 30);
        assertEquals(metrics.get("avgReferences"), 1.36);
        assertEquals(metrics.get("maxReferences"), 5);
  }

    public void testVariableIdentificationIdentification(){
       VariablesIdentifier variablesIdentifier = new VariablesIdentifier();
       metrics = variablesIdentifier.updateMetric(metrics, identifiedBlock);

       assertEquals(metrics.get("numVars"), 34);
       assertEquals(metrics.get("avgNumVars"), 1.55);
       assertEquals(metrics.get("maxNumVars"), 8);
    }

    public void testSplatExpressionIdentification(){
      SplatExpressionIdentifier splatExpressionIdentifier = new SplatExpressionIdentifier();
      metrics = splatExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numSplatExpressions"), 2);
      assertEquals(metrics.get("avgSplatExpressions"), 0.09);
      assertEquals(metrics.get("maxSplatExpressions"), 1);
    }

    public void testTemplateExpressionIdentification(){
        TemplateExpressionIdentifier templateExpressionIdentifier = new TemplateExpressionIdentifier();
        metrics = templateExpressionIdentifier.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numTemplateExpression"), 3);
        assertEquals(metrics.get("avgTemplateExpression"), 0.14);

    }

    public void testTextEntropyMeasure(){
      TokenIdentifier tokenIdentifier = new TokenIdentifier();
      metrics = tokenIdentifier.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("textEntropyMeasure"),  5.42);
      assertEquals(metrics.get("minAttrsTextEntropy"), 2.85);
      assertEquals(metrics.get("maxAttrsTextEntropy"), 5.48);
      assertEquals(metrics.get("avgAttrsTextEntropy"), 4.18);
      assertEquals(metrics.get("numTokens"), 292);
      assertEquals(metrics.get("minTokensPerAttr"), 3);
      assertEquals(metrics.get("maxTokensPerAttr"), 34);
      assertEquals(metrics.get("avgTokensPerAttr"), 12.55);

    }

    public void testTuplesIdentification() {
        TupleIdentifier tupleIdentifier = new TupleIdentifier();
        metrics = tupleIdentifier.updateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numTuples"), 7);
        assertEquals(metrics.get("avgTuples"), 0.3181818181818182);
        assertEquals(metrics.get("maxTuples"), 2);

    }

    public void testTupleElementsIdentification() {
      TupleIdentifier tupleIdentifier = new TupleIdentifier();

      List<TerraformTreeImpl> tuples = tupleIdentifier.filterTuplesFromBlock(identifiedBlock);
      TupleElementsIdentifier tupleElementsIdentifier = new TupleElementsIdentifier(tuples);
      metrics = tupleElementsIdentifier.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numElemTuples"), 12);
      assertEquals(metrics.get("avgElemTuples"), 1.7142857142857142);
      assertEquals(metrics.get("maxElemTuples"), 5);
    }

    public void testComplexityBlock() {
      BlockComplexity blockComplexity = new BlockComplexity("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\base.tf", identifiedBlock);
      metrics = blockComplexity.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("depthOfBlock"), 156);
      assertEquals(metrics.get("loc"), 132);
      assertEquals(metrics.get("nloc"), 24);

    }

    public void testBlockCheckerType() {
      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();
      metrics = blockCheckerType.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("isResource"), 1);
      assertEquals(metrics.get("isModule"), 0);
      assertEquals(metrics.get("isData"), 0);
      assertEquals(metrics.get("isTerraform"), 0);
      assertEquals(metrics.get("isProvider"), 0);
      assertEquals(metrics.get("isVariable"), 0);
      assertEquals(metrics.get("isOutput"), 0);
      assertEquals(metrics.get("isLocals"), 0);
      assertEquals(metrics.get("containDescriptionField"), 0);
    }

    public void testNumberOfAttributes(){
      AttrFinderImpl attrFinder = new AttrFinderImpl();
      metrics = attrFinder.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numAttrs"), 22);
    }

    public void testResourceDependency(){
      ResourceDependency resourceDependency = new ResourceDependency();
      metrics = resourceDependency.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numResourceDependency"), 0);
    }


}

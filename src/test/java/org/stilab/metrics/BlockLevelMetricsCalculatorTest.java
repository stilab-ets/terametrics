package org.stilab.metrics;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.collectors.*;
import org.stilab.visitors.BlockComplexity;
import org.stilab.visitors.BlockMetaInfo;
import org.stilab.visitors.TopBlockFinder;

import java.io.File;
import java.util.List;


public class BlockLevelMetricsCalculatorTest extends TestCase {

   private BlockTreeImpl identifiedBlock;
   private JSONObject metrics;
   private BlockComplexity blockComplexity;
   private String filePath;

  @Override
  protected void setUp() throws Exception {

    super.setUp();
    String tfFilePath = "src/test/java/org/stilab/metrics/data/base.tf";

    File base = new File(tfFilePath);
    HclParser hclParser = new HclParser();
    Tree tree = hclParser.parse(base);

    // To look for the Parsed One
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
    identifiedBlock = blocks.get(0);
    metrics = new JSONObject();
    blockComplexity = new BlockComplexity(tfFilePath, identifiedBlock);
    filePath = tfFilePath;
  }

  public void testBlockMetaInfoIdentification() {
    BlockMetaInfo metaInfo = new BlockMetaInfo();
    metrics = metaInfo.updateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("impacted_block_type"), "aws_elastic_beanstalk_environment tfenvtest");
    assertEquals(metrics.get("block"), "resource");
    assertEquals(metrics.get("start_block"), 1);
    assertEquals(metrics.get("end_block"), 157);
    assertEquals(metrics.get("block_identifiers"), "resource aws_elastic_beanstalk_environment tfenvtest");
  }

  public void testComparisonOperatorsIdentification(){
    ComparisonOperatorsCollector comparisonOperatorsCollector = new ComparisonOperatorsCollector();
    metrics = comparisonOperatorsCollector.decorateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("numComparisonOperators"), 1);
    assertEquals(metrics.get("avgComparisonOperators"), 0.04);
    assertEquals(metrics.get("maxComparisonOperators"), 1);
  }

  public void testConditionalExpressionIdentification(){
    ConditionalExpressionCollector conditionalExpressionCollector = new ConditionalExpressionCollector();
    metrics = conditionalExpressionCollector.decorateMetric(metrics, identifiedBlock);

    assertEquals(metrics.get("numConditions"), 2);
    assertEquals(metrics.get("avgConditions"), 0.09);
    assertEquals(metrics.get("maxConditions"), 1);
  }

    public void testLogicalOperatorsIdentification(){
      LogicalOperationsCollector logicalOperationsCollector = new LogicalOperationsCollector();
      metrics = logicalOperationsCollector.decorateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numLogiOpers"), 5);
      assertEquals(metrics.get("avgLogiOpers"), 0.22);
      assertEquals(metrics.get("maxLogiOpers"), 4);
    }

    public void testDynamicBlocksIdentification(){
      DynamicBlocksCollector dynamicBlocksCollector = new DynamicBlocksCollector();
      metrics = dynamicBlocksCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDynamicBlocks"), 1);
    }

    public void testLookUpFunctionCallIdentification() {
      LookUpFunctionCollector lookUpFunctionCollector = new LookUpFunctionCollector();
      metrics = lookUpFunctionCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLookUpFunctionCall"), 0);
    }

    public void testDeprecatedFunctionsIdentification() {
      DeprecatedFunctionsCollector deprecatedFunctionsCollector = new DeprecatedFunctionsCollector();
      metrics = deprecatedFunctionsCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDeprecatedFunctions"), 0);
    }

    public void testDebuggingFunctionsIdentification() {
      DebuggingFunctionCollector debuggingFunctionCollector = new DebuggingFunctionCollector();
      metrics = debuggingFunctionCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDebuggingFunctions"), 2);
    }

    public void testNestedBlocksIdentification(){
      NestedBlockCollector nestedBlockCollector = new NestedBlockCollector();
      metrics = nestedBlockCollector.decorateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numNestedBlocks"), 3);
      assertEquals(metrics.get("avgDepthNestedBlocks"), 16.33);
      assertEquals(metrics.get("maxDepthNestedBlocks"), 36);
      assertEquals(metrics.get("minDepthNestedBlocks"), 5);
    }

    public void testFuncCallIdentification(){

      FunctionCallExpressionCollector functionCallExpressionCollector = new FunctionCallExpressionCollector();
      metrics = functionCallExpressionCollector.decorateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numFunctionCall"), 9);
      assertEquals(metrics.get("avgFunctionCall"), 0.39);
      assertEquals(metrics.get("maxFunctionCall"), 2);
    }

    public void testParametersIdentification(){

      FunctionParametersCollector functionParametersCollector = new FunctionParametersCollector();
      metrics = functionParametersCollector.decorateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numParams"), 17);
      assertEquals(metrics.get("avgParams"), 1.89);
      assertEquals(metrics.get("maxParams"), 3);

    }

    public void testHereDocIdentification(){
      HereDocCollector hereDocCollector = new HereDocCollector();
      metrics = hereDocCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numHereDocs"), 4);
      assertEquals(metrics.get("avgHereDocs"), 0.17);
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
        IndexAccessCollector indexAccessCollector = new IndexAccessCollector();
        metrics = indexAccessCollector.decorateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numIndexAccess"), 2);
        assertEquals(metrics.get("avgIndexAccess"), 0.09);
        assertEquals(metrics.get("maxIndexAccess"), 1);
    }

    public void testLiteralExpressionIdentification(){

      LiteralExpressionCollector literalExpressionCollector = new LiteralExpressionCollector();
      metrics = literalExpressionCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLiteralExpression"), 29);
      //  Number of String values means number of hard coded things
      assertEquals(metrics.get("numStringValues"), 16);
      //  SUM of Length of literal Expressions (in term Of tokens)
//      assertEquals(metrics.get("sumLengthStringValues"), 3899);
//      //  Average Length Of Literal Expressions (in term of Tokens)
//      assertEquals(metrics.get("avgLengthStringValues"), 243.69);
//      //  MAX Length Of Literal Expressions (in term of tokens)
//      assertEquals(metrics.get("maxLengthStringValues"), 1717);

    }

    public void testLoopsExpressionIdentification() {

        LoopsExpressionCollector loopsExpressionCollector = new LoopsExpressionCollector();
        metrics = loopsExpressionCollector.decorateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numLoops"), 3);
        assertEquals(metrics.get("avgLoops"), 0.13);
        assertEquals(metrics.get("maxLoops"), 2);
    }

    public void testMathOperatorsIdentification(){
      MathOperationsCollector mathOperationsCollector = new MathOperationsCollector();
      metrics = mathOperationsCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numMathOperations"), 2);
      assertEquals(metrics.get("avgMathOperations"), 0.09);
      assertEquals(metrics.get("maxMathOperations"), 2);

    }

    public void testMCCabCCIdentification() {
        MccabeCCCollector mccabeCCCollector = new MccabeCCCollector();
        metrics = mccabeCCCollector.decorateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("avgMccabeCC"), 1.22);
        assertEquals(metrics.get("sumMccabeCC"), 28);
        assertEquals(metrics.get("maxMccabeCC"), 3);
    }

    public void testMetaArgumentsIdentification() {
        MetaArgumentCollector metaArgumentCollector = new MetaArgumentCollector();
        metrics = metaArgumentCollector.decorateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numMetaArg"), 2);
    }

    public void testObjectWrapperIdentification() {

      ObjectWrapperCollector objectWrapperCollector = new ObjectWrapperCollector();
      metrics = objectWrapperCollector.decorateMetric(metrics, identifiedBlock);
       assertEquals(metrics.get("numObjects"), 5);
       assertEquals(metrics.get("avgObjects"), 0.22);
       assertEquals(metrics.get("maxObjects"), 2);
    }

    public void testObjectWrapperElementsIdentification(){

      ObjectWrapperElementCollector objectWrapperElementCollector = new ObjectWrapperElementCollector();
      metrics = objectWrapperElementCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numElemObjects"), 7);
      assertEquals(metrics.get("avgElemObjects"), 1.4);
      assertEquals(metrics.get("maxElemObjects"), 2);

    }

    public void testReferenceIdentificationIdentification(){
        ReferenceCollector referenceCollector = new ReferenceCollector();
        metrics = referenceCollector.decorateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numReferences"), 34);
        assertEquals(metrics.get("avgReferences"), 1.48);
        assertEquals(metrics.get("maxReferences"), 5);
  }

    public void testVariableIdentificationIdentification(){
       VariablesCollector variablesCollector = new VariablesCollector();
       metrics = variablesCollector.decorateMetric(metrics, identifiedBlock);

       assertEquals(metrics.get("numVars"), 35);
       assertEquals(metrics.get("avgNumVars"), 1.52);
       assertEquals(metrics.get("maxNumVars"), 8);
    }

    public void testSplatExpressionIdentification(){

      SplatExpressionCollector splatExpressionCollector = new SplatExpressionCollector();

      metrics = splatExpressionCollector.decorateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numSplatExpressions"), 2);
      assertEquals(metrics.get("avgSplatExpressions"), 0.09);
      assertEquals(metrics.get("maxSplatExpressions"), 1);
    }

    public void testTemplateExpressionIdentification(){
        TemplateExpressionCollector templateExpressionCollector = new TemplateExpressionCollector();

        metrics = templateExpressionCollector.decorateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numTemplateExpression"), 3);
        assertEquals(metrics.get("avgTemplateExpression"), 0.13);

    }

    public void testTextEntropyMeasure(){
      TokenCollector tokenCollector = new TokenCollector();

      metrics = tokenCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("textEntropyMeasure"),  5.41);
      assertEquals(metrics.get("minAttrsTextEntropy"), 2.85);
      assertEquals(metrics.get("maxAttrsTextEntropy"), 5.48);
      assertEquals(metrics.get("avgAttrsTextEntropy"), 4.18);
      assertEquals(metrics.get("numTokens"), 303);
      assertEquals(metrics.get("minTokensPerAttr"), 3);
      assertEquals(metrics.get("maxTokensPerAttr"), 34);
      assertEquals(metrics.get("avgTokensPerAttr"), 12.48);

    }

    public void testTuplesIdentification() {

        TupleCollector tupleCollector = new TupleCollector();
        metrics = tupleCollector.decorateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numTuples"), 7);
        assertEquals(metrics.get("avgTuples"), 0.3);
        assertEquals(metrics.get("maxTuples"), 2);

    }

    public void testTupleElementsIdentification() {

      TupleElementsCollector tupleElementsCollector = new TupleElementsCollector();
      metrics = tupleElementsCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numElemTuples"), 12);
      assertEquals(metrics.get("avgElemTuples"), 1.71);
      assertEquals(metrics.get("maxElemTuples"), 5);
    }

    public void testComplexityBlock() {

      BlockComplexityCollector blockComplexityCollector = new BlockComplexityCollector(blockComplexity.getBlockContent());

      metrics = blockComplexityCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("depthOfBlock"), 157);
      assertEquals(metrics.get("loc"), 133);
      assertEquals(metrics.get("nloc"), 24);
    }

    public void testBlockCheckerType() {

      BlockCheckTypeCollector blockCheckTypeCollector = new BlockCheckTypeCollector();
      metrics = blockCheckTypeCollector.decorateMetric(metrics, identifiedBlock);

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
      AttributesCollector attrFinder = new AttributesCollector();
      metrics = attrFinder.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numAttrs"), 23);
    }

    public void testExplicitResourceDependency(){
      ExplicitResourceDependencyCollector explicitResourceDependencyCollector = new ExplicitResourceDependencyCollector();
      metrics = explicitResourceDependencyCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numExplicitResourceDependency"), 0);
    }

    public void testImplicitResourceDependency(){

      ImplicitResourceCollector implicitResourceCollector = new ImplicitResourceCollector();

      metrics = implicitResourceCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numImplicitDependentResources"), 3);
      assertEquals(metrics.get("numImplicitDependentData"), 1);
      assertEquals(metrics.get("numImplicitDependentModules"), 0);
      assertEquals(metrics.get("numImplicitDependentProviders"), 0);
      assertEquals(metrics.get("numImplicitDependentLocals"), 2);
      assertEquals(metrics.get("numImplicitDependentVars"), 10);
      assertEquals(metrics.get("numImplicitDependentEach"), 2);
    }

    public void testEmptyStringIdentification() {

      SpecialStringCollector specialStringCollector = new SpecialStringCollector();
      metrics = specialStringCollector.decorateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numEmptyString"), 1);
      assertEquals(metrics.get("numWildCardSuffixString"), 1);
      assertEquals(metrics.get("numStarString"), 1);
   }

}

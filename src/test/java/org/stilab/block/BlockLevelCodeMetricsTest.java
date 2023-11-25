package org.stilab.block;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.counter.block.counter.BlockCheckerTypeImpl;
import org.stilab.metrics.counter.block.data.repository.*;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.block.visitors.*;
import org.stilab.metrics.counter.block.visitors.NestedBlockVisitor;
import org.stilab.metrics.counter.block.iterators.TopBlockFinder;
import org.stilab.metrics.counter.block.visitors.ImplicitResourceDependencyVisitor;
import org.stilab.metrics.counter.block.visitors.deprecation.cloud.DeprecatoryServiceLocator;

import java.io.File;
import java.util.List;


public class BlockLevelCodeMetricsTest extends TestCase {

   private BlockTreeImpl identifiedBlock;
   private JSONObject metrics;
   private BlockComplexity blockComplexity;
   private String filePath;

  @Override
  protected void setUp() throws Exception {

    super.setUp();
    String tfFilePath = "src/test/java/org/stilab/block/data/base.tf";

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
    ComparisonOperatorsRepository comparisonOperatorsRepository = new ComparisonOperatorsRepository();
    metrics = comparisonOperatorsRepository.updateMetric(metrics, identifiedBlock);
    assertEquals(metrics.get("numComparisonOperators"), 1);
    assertEquals(metrics.get("avgComparisonOperators"), 0.04);
    assertEquals(metrics.get("maxComparisonOperators"), 1);
  }

  public void testConditionalExpressionIdentification(){
//    TODO: review this later before reviewing how to measure the complexity of a Block
    ConditionalExpressionRepository conditionalExpressionRepository = new ConditionalExpressionRepository();
    metrics = conditionalExpressionRepository.updateMetric(metrics, identifiedBlock);

    assertEquals(metrics.get("numConditions"), 2);
    assertEquals(metrics.get("avgConditions"), 0.09);
    assertEquals(metrics.get("maxConditions"), 1);
  }

    public void testLogicalOperatorsIdentification(){
      LogicalOperationsRepository logicalOperationsRepository = new LogicalOperationsRepository();
      metrics = logicalOperationsRepository.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numLogiOpers"), 5);
      assertEquals(metrics.get("avgLogiOpers"), 0.22);
      assertEquals(metrics.get("maxLogiOpers"), 4);
    }

    public void testDynamicBlocksIdentification(){
      DynamicBlocksRepository dynamicBlocksRepository = new DynamicBlocksRepository();
      metrics = dynamicBlocksRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDynamicBlocks"), 1);
    }

    public void testLookUpFunctionCallIdentification() {
      LookUpFunctionRepository lookUpFunctionRepository = new LookUpFunctionRepository();
      metrics = lookUpFunctionRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLookUpFunctionCall"), 0);
    }

    public void testDeprecatedFunctionsIdentification() {
      DeprecatedFunctionsRepository deprecatedFunctionsRepository = new DeprecatedFunctionsRepository();
      metrics = deprecatedFunctionsRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDeprecatedFunctions"), 0);
    }

    public void testDebuggingFunctionsIdentification() {
      DebuggingFunctionRepository debuggingFunctionRepository = new DebuggingFunctionRepository();
      metrics = debuggingFunctionRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numDebuggingFunctions"), 2);
    }

    public void testNestedBlocksIdentification(){
      NestedBlockRepository nestedBlockRepository = new NestedBlockRepository();
      metrics = nestedBlockRepository.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numNestedBlocks"), 3);
      assertEquals(metrics.get("avgDepthNestedBlocks"), 16.33);
      assertEquals(metrics.get("maxDepthNestedBlocks"), 36);
      assertEquals(metrics.get("minDepthNestedBlocks"), 5);
    }

    public void testFuncCallIdentification(){

      FunctionCallExpressionRepository functionCallExpressionRepository = new FunctionCallExpressionRepository();
      metrics = functionCallExpressionRepository.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numFunctionCall"), 9);
      assertEquals(metrics.get("avgFunctionCall"), 0.39);
      assertEquals(metrics.get("maxFunctionCall"), 2);
    }

    public void testParametersIdentification(){

      FunctionParametersRepository functionParametersRepository = new FunctionParametersRepository();
      metrics = functionParametersRepository.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numParams"), 17);
      assertEquals(metrics.get("avgParams"), 1.89);
      assertEquals(metrics.get("maxParams"), 3);

    }

    public void testHereDocIdentification(){
      HereDocRepository hereDocRepository = new HereDocRepository();
      metrics = hereDocRepository.updateMetric(metrics, identifiedBlock);
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
        IndexAccessRepository indexAccessRepository = new IndexAccessRepository();
        metrics = indexAccessRepository.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numIndexAccess"), 2);
        assertEquals(metrics.get("avgIndexAccess"), 0.09);
        assertEquals(metrics.get("maxIndexAccess"), 1);
    }

    public void testLiteralExpressionIdentification(){

      LiteralExpressionRepository literalExpressionRepository = new LiteralExpressionRepository();
      metrics = literalExpressionRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numLiteralExpression"), 29);
      //  Number of String values means number of hard coded things
      assertEquals(metrics.get("numStringValues"), 16);
      //  SUM of Length of literal Expressions (in term Of tokens)
      assertEquals(metrics.get("sumLengthStringValues"), 3899);
      //  Average Length Of Literal Expressions (in term of Tokens)
      assertEquals(metrics.get("avgLengthStringValues"), 243.69);
      //  MAX Length Of Literal Expressions (in term of tokens)
      assertEquals(metrics.get("maxLengthStringValues"), 1717);

    }

    public void testLoopsExpressionIdentification() {
        LoopsExpressionRepository loopsExpressionRepository = new LoopsExpressionRepository();
        metrics = loopsExpressionRepository.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numLoops"), 3);
        assertEquals(metrics.get("avgLoops"), 0.13);
        assertEquals(metrics.get("maxLoops"), 2);
    }

    public void testMathOperatorsIdentification(){
      MathOperationsRepository mathOperationsRepository = new MathOperationsRepository();
      metrics = mathOperationsRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numMathOperations"), 2);
      assertEquals(metrics.get("avgMathOperations"), 0.09);
      assertEquals(metrics.get("maxMathOperations"), 2);

    }

    public void testMCCabCCIdentification() {
        MccabeCCRepository mccabeCCRepository = new MccabeCCRepository();
        metrics = mccabeCCRepository.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("avgMccabeCC"), 1.22);
        assertEquals(metrics.get("sumMccabeCC"), 28);
        assertEquals(metrics.get("maxMccabeCC"), 3);
    }

    public void testMetaArgumentsIdentification() {
        MetaArgumentRepository metaArgumentRepository = new MetaArgumentRepository();
        metrics = metaArgumentRepository.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numMetaArg"), 2);
    }

    public void testObjectWrapperIdentification() {
      ObjectWrapperRepository objectWrapperRepository = new ObjectWrapperRepository();
      metrics = objectWrapperRepository.updateMetric(metrics, identifiedBlock);
       assertEquals(metrics.get("numObjects"), 5);
       assertEquals(metrics.get("avgObjects"), 0.22);
       assertEquals(metrics.get("maxObjects"), 2);
    }

    public void testObjectWrapperElementsIdentification(){
      ObjectWrapperVisitor objectWrapperVisitor = new ObjectWrapperVisitor();
      List<TerraformTreeImpl> objects = objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);


      ObjectWrapperElementRepository objectWrapperElementRepository = new ObjectWrapperElementRepository(objects);
      metrics = objectWrapperElementRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numElemObjects"), 7);
      assertEquals(metrics.get("avgElemObjects"), 1.4);
      assertEquals(metrics.get("maxElemObjects"), 2);

    }

    public void testReferenceIdentificationIdentification(){
        ReferenceRepository referenceRepository = new ReferenceRepository();
        metrics = referenceRepository.updateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numReferences"), 34);
        assertEquals(metrics.get("avgReferences"), 1.48);
        assertEquals(metrics.get("maxReferences"), 5);
  }

    public void testVariableIdentificationIdentification(){
       VariablesRepository variablesRepository = new VariablesRepository();
       metrics = variablesRepository.updateMetric(metrics, identifiedBlock);

       assertEquals(metrics.get("numVars"), 35);
       assertEquals(metrics.get("avgNumVars"), 1.52);
       assertEquals(metrics.get("maxNumVars"), 8);
    }

    public void testSplatExpressionIdentification(){

      SplatExpressionRepository splatExpressionRepository = new SplatExpressionRepository();

      metrics = splatExpressionRepository.updateMetric(metrics, identifiedBlock);

      assertEquals(metrics.get("numSplatExpressions"), 2);
      assertEquals(metrics.get("avgSplatExpressions"), 0.09);
      assertEquals(metrics.get("maxSplatExpressions"), 1);
    }

    public void testTemplateExpressionIdentification(){
        TemplateExpressionRepository templateExpressionRepository = new TemplateExpressionRepository();

        metrics = templateExpressionRepository.updateMetric(metrics, identifiedBlock);
        assertEquals(metrics.get("numTemplateExpression"), 3);
        assertEquals(metrics.get("avgTemplateExpression"), 0.13);

    }

    public void testTextEntropyMeasure(){
      TokenRepository tokenRepository = new TokenRepository();

      metrics = tokenRepository.updateMetric(metrics, identifiedBlock);
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

        TupleRepository tupleRepository = new TupleRepository();
        metrics = tupleRepository.updateMetric(metrics, identifiedBlock);

        assertEquals(metrics.get("numTuples"), 7);
        assertEquals(metrics.get("avgTuples"), 0.3);
        assertEquals(metrics.get("maxTuples"), 2);

    }

    public void testTupleElementsIdentification() {

      TupleVisitor tupleVisitor = new TupleVisitor();
      List<TerraformTreeImpl> tuples = tupleVisitor.filterTuplesFromBlock(identifiedBlock);

      TupleElementsRepository tupleElementsRepository = new TupleElementsRepository(tuples);
      metrics = tupleElementsRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numElemTuples"), 12);
      assertEquals(metrics.get("avgElemTuples"), 1.71);
      assertEquals(metrics.get("maxElemTuples"), 5);
    }

    public void testComplexityBlock() {

      BlockComplexityRepository blockComplexityRepository = new BlockComplexityRepository(filePath);

      metrics = blockComplexityRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("depthOfBlock"), 157);
      assertEquals(metrics.get("loc"), 133);
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
      assertEquals(metrics.get("numAttrs"), 23);
    }

    public void testExplicitResourceDependency(){
      ExplicitResourceDependencyRepository explicitResourceDependencyRepository = new ExplicitResourceDependencyRepository();
      metrics = explicitResourceDependencyRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numExplicitResourceDependency"), 0);
    }

    public void testImplicitResourceDependency(){

      ImplicitResourceRepository implicitResourceRepository = new ImplicitResourceRepository();

      metrics = implicitResourceRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numImplicitDependentResources"), 3);
      assertEquals(metrics.get("numImplicitDependentData"), 1);
      assertEquals(metrics.get("numImplicitDependentModules"), 0);
      assertEquals(metrics.get("numImplicitDependentProviders"), 0);
      assertEquals(metrics.get("numImplicitDependentLocals"), 2);
      assertEquals(metrics.get("numImplicitDependentVars"), 10);
      assertEquals(metrics.get("numImplicitDependentEach"), 2);
    }

    public void testEmptyStringIdentification() {
      LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();

      SpecialStringRepository specialStringRepository = new SpecialStringRepository(literalExpressionVisitor);
      metrics = specialStringRepository.updateMetric(metrics, identifiedBlock);
      assertEquals(metrics.get("numEmptyString"), 1);
      assertEquals(metrics.get("numWildCardSuffixString"), 1);
      assertEquals(metrics.get("numStarString"), 1);
   }

}

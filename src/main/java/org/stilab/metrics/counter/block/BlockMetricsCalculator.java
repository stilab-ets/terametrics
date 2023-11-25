package org.stilab.metrics.counter.block;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.io.FileWriter;
import java.io.IOException;

public class BlockMetricsCalculator {

    public JSONObject measureMetrics(BlockTreeImpl identifiedBlock, String blockAsString) {

      JSONObject metrics = new JSONObject();

      // Meta Infos that concern the identified Block
//      BlockMetaInfo metaInfo = new BlockMetaInfo();
//      // "impacted_block_type" "block" "start_block" "end_block" "block_identifiers"
//      metrics = metaInfo.updateMetric(metrics, identifiedBlock);
//
//      // Comparison Operators
//      // "numComparisonOperators" "avgComparisonOperators" "maxComparisonOperators"
//      ComparisonOperatorsVisitor cmp = new ComparisonOperatorsVisitor();
//      metrics = cmp.updateMetric(metrics, identifiedBlock);
//
//      // Conditional Expressions
//      // "numConditions" "avgConditions" "maxConditions"
//      ConditionalExpressionVisitor conditionalExpressionVisitor = new ConditionalExpressionVisitor();
//      metrics = conditionalExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Logical Operators
//      // "numLogiOpers" "avgLogiOpers" "maxLogiOpers"
//      LogicalOperationsVisitor logOper = new LogicalOperationsVisitor();
//      metrics = logOper.updateMetric(metrics, identifiedBlock);
//
//      // Dynamic Blocks
//      // "numDynamicBlocks"
//      DynamicBlocksVisitor dynamicBlocksVisitor = new DynamicBlocksVisitor();
//      metrics = dynamicBlocksVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Nested Blocks
//      // "numNestedBlocks"
//      NestedBlockVisitor nestedBlockIdentifier = new NestedBlockVisitor();
//      metrics = nestedBlockIdentifier.updateMetric(metrics, identifiedBlock);
//
//      // Function Calls
//      // "numFunctionCall" "avgFunctionCall" "maxFunctionCall"
//      FunctionCallExpressionVisitor functionCallExpressionVisitor = new FunctionCallExpressionVisitor();
//      metrics = functionCallExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Lookup Function Calls
//      // ADDED:::: "numLookUpFunctionCalls"
//      LookUpFunctionVisitor lookUpFunctionVisitor = new LookUpFunctionVisitor(functionCallExpressionVisitor);
//      metrics = lookUpFunctionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Deprecated Functions
//      // ADDED:::: "numDeprecatedFunctions"
//      DeprecatedFunctionsVisitor deprecatedFunctionsVisitor = new DeprecatedFunctionsVisitor(functionCallExpressionVisitor);
//      metrics = deprecatedFunctionsVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Debugging Functions
//      // ADDED:::: "numDebuggingFunctions"
//      DebuggingFunctionVisitor debuggingFunctionVisitor = new DebuggingFunctionVisitor(functionCallExpressionVisitor);
//      metrics = debuggingFunctionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Implicit Resource Dependency
//      // ADDED:::: "numDependentResources" "numDependentData" "numDependentModules" "numDependentProviders"
//      // ADDED:::: "numDependentProviders" "numDependentLocals" "numDependentVars"
//      ImplicitResourceDependencyVisitor implicitResourceDependencyVisitor = new ImplicitResourceDependencyVisitor();
//      metrics = implicitResourceDependencyVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Number of Resource Dependency
//      ExplicitResourceDependencyVisitor explicitResourceDependencyVisitor = new ExplicitResourceDependencyVisitor();
//      metrics = explicitResourceDependencyVisitor.updateMetric(metrics, identifiedBlock);
//
//      // function Parameters
//      // "numParams" "avgParams" "maxParams"
//      FunctionParametersVisitor functionParametersVisitor = new FunctionParametersVisitor();
//      metrics = functionParametersVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Here Docs
//      // "numHereDocs" "avgHereDocs" "numLinesHereDocs" "avgLinesHereDocs" "maxLinesHereDocs"
//      HereDocVisitor hereDocVisitor = new HereDocVisitor();
//      metrics = hereDocVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Index Access
//      // "numIndexAccess" "avgIndexAccess" "maxIndexAccess"
//      IndexAccessVisitor indexAccessIdentifier = new IndexAccessVisitor();
//      metrics = indexAccessIdentifier.updateMetric(metrics, identifiedBlock);
//
//      // Literal Expressions
//      // "numLiteralExpression" "numStringValues" "sumLengthStringValues" "avgLengthStringValues" "maxLengthStringValues"
//      LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();
//      metrics = literalExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // ADDED:: numEmptyString
//      SpecialStringVisitor specialStringVisitor = new SpecialStringVisitor(literalExpressionVisitor);
//      metrics = specialStringVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Loops Expressions
//      // "numLoops" "avgLoops" "maxLoops"
//      LoopsExpressionVisitor loopsExpressionVisitor = new LoopsExpressionVisitor();
//      metrics = loopsExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Math Operation
//      // "numMathOperations" "avgMathOperations" "maxMathOperations"
//      MathOperationsVisitor mathOperationsVisitor = new MathOperationsVisitor();
//      metrics = mathOperationsVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Complexity
//      // "avgMccabeCC" "sumMccabeCC" "maxMccabeCC"
//      MccabeCC mccabeCC = new MccabeCC();
//      metrics = mccabeCC.updateMetric(metrics, identifiedBlock);
//
//      // Meta-Arguments
//      // "numMetaArg"
//      MetaArgumentVisitor metaArgumentVisitor = new MetaArgumentVisitor();
//      metrics = metaArgumentVisitor.updateMetrics(metrics, identifiedBlock);
//
//      // Objects
//      // "numObjects" "avgObjects" "maxTuples"
//      ObjectWrapperVisitor objectWrapperVisitor = new ObjectWrapperVisitor();
//      metrics = objectWrapperVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Objects Elements
//      // "numElemTuples" "avgElemTuples" "maxElemTuples"
//      List<TerraformTreeImpl> objects = objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);
//      ObjectWrapperElementVisitor objectWrapperElementVisitor = new ObjectWrapperElementVisitor(objects);
//      metrics = objectWrapperElementVisitor.updateMetric(metrics, identifiedBlock);
//
//      // References (pointers)
//      // "numReferences" "avgReferences" "maxReferences"
//      ReferenceVisitor referenceVisitor = new ReferenceVisitor();
//      metrics = referenceVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Variables
//      // "numVars" "avgNumVars" "maxNumVars"
//      VariablesVisitor variablesVisitor = new VariablesVisitor();
//      metrics = variablesVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Splat Expressions
//      // "numSplatExpressions" "avgSplatExpressions" "maxSplatExpressions"
//      SplatExpressionVisitor splatExpressionVisitor = new SplatExpressionVisitor();
//      metrics = splatExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Template Expression
//      // "numTemplateExpression" "avgTemplateExpression"
//      TemplateExpressionVisitor templateExpressionVisitor = new TemplateExpressionVisitor();
//      metrics = templateExpressionVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Tokens
//      // "textEntropyMeasure" "minAttrsTextEntropy" "maxAttrsTextEntropy" "avgAttrsTextEntropy" "numTokens"
//      // "minTokensPerAttr" "maxTokensPerAttr" "avgTokensPerAttr"
//      TokenVisitor tokenVisitor = new TokenVisitor();
//      metrics = tokenVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Tuples
//      // "numTuples" "avgTuples" "maxTuples"
//      TupleVisitor tupleVisitor = new TupleVisitor();
//      metrics = tupleVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Tuples Elements
//      // "numElemTuples" "avgElemTuples" "maxElemTuples"
//      List<TerraformTreeImpl> tuples = tupleVisitor.filterTuplesFromBlock(identifiedBlock);
//      TupleElementsVisitor tupleElementsVisitor = new TupleElementsVisitor(tuples);
//      metrics = tupleElementsVisitor.updateMetric(metrics, identifiedBlock);
//
//      // Size of Block
//      // "depthOfBlock" "loc" "nloc"
//      BlockComplexity blockComplexity = new BlockComplexity(identifiedBlock, blockAsString);
//      metrics = blockComplexity.updateMetric(metrics, identifiedBlock);
//
//      // Check the type of the studied block
//      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();
//      metrics = blockCheckerType.updateMetric(metrics, identifiedBlock);
//
//      // Number of Attributes
//      AttrFinderImpl attrFinder = new AttrFinderImpl();
//      metrics = attrFinder.updateMetric(metrics, identifiedBlock);

      return metrics;
    }

    public void writeMetricsAsJsonFile(BlockTreeImpl blockTree, String blockAsString, String filePath) {
      JSONObject metrics = measureMetrics(blockTree, blockAsString);
      //Write JSON file
      try (FileWriter file = new FileWriter(filePath)) {
        //We can write any JSONArray or JSONObject instance to the file
        file.write(metrics.toJSONString());
        file.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}

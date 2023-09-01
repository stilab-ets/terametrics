package org.stilab.utils;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block.metrics.*;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.metrics.block_dependency.ImplicitResourceDependency;
import org.stilab.metrics.counter.block.metrics.deprecation.DeprecatedFunctionsIdentifier;
import org.stilab.metrics.counter.block.metrics.deprecation.cloud.DeprecatoryServiceLocator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MetricsCalculator {

    public MetricsCalculator() {  }

    public JSONObject measureMetrics(BlockTreeImpl identifiedBlock, String blockAsString) {

      JSONObject metrics = new JSONObject();

      // Meta Infos that concern the identified Block
      BlockMetaInfo metaInfo = new BlockMetaInfo();
      // "impacted_block_type" "block" "start_block" "end_block" "block_identifiers"
      metrics = metaInfo.updateMetric(metrics, identifiedBlock);

      // Comparison Operators
      // "numComparisonOperators" "avgComparisonOperators" "maxComparisonOperators"
      ComparisonOperatorsIdentifier cmp = new ComparisonOperatorsIdentifier();
      metrics = cmp.updateMetric(metrics, identifiedBlock);

      // Conditional Expressions
      // "numConditions" "avgConditions" "maxConditions"
      ConditionalExpressionIdentifier conditionalExpressionIdentifier = new ConditionalExpressionIdentifier();
      metrics = conditionalExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // Logical Operators
      // "numLogiOpers" "avgLogiOpers" "maxLogiOpers"
      LogicalOperationsIdentifier logOper = new LogicalOperationsIdentifier();
      metrics = logOper.updateMetric(metrics, identifiedBlock);

      // Dynamic Blocks
      // "numDynamicBlocks"
      DynamicBlocksIdentifier dynamicBlocksIdentifier = new DynamicBlocksIdentifier();
      metrics = dynamicBlocksIdentifier.updateMetric(metrics, identifiedBlock);

      // Nested Blocks
      // "numNestedBlocks"
      NestedBlockIdentifier nestedBlockIdentifier = new NestedBlockIdentifier();
      metrics = nestedBlockIdentifier.updateMetric(metrics, identifiedBlock);

      // Function Calls
      // "numFunctionCall" "avgFunctionCall" "maxFunctionCall"
      FunctionCallExpressionIdentifier functionCallExpressionIdentifier = new FunctionCallExpressionIdentifier();
      metrics = functionCallExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // Lookup Function Calls
      // ADDED:::: "numLookUpFunctionCalls"
      LookUpFunctionIdentifier lookUpFunctionIdentifier = new LookUpFunctionIdentifier(functionCallExpressionIdentifier);
      metrics = lookUpFunctionIdentifier.updateMetric(metrics, identifiedBlock);

      // Deprecated Functions
      // ADDED:::: "numDeprecatedFunctions"
      DeprecatedFunctionsIdentifier deprecatedFunctionsIdentifier = new DeprecatedFunctionsIdentifier(functionCallExpressionIdentifier);
      metrics = deprecatedFunctionsIdentifier.updateMetric(metrics, identifiedBlock);

      // Debugging Functions
      // ADDED:::: "numDebuggingFunctions"
      DebuggingFunctionIdentifier debuggingFunctionIdentifier = new DebuggingFunctionIdentifier(functionCallExpressionIdentifier);
      metrics = debuggingFunctionIdentifier.updateMetric(metrics, identifiedBlock);

      // Implicit Resource Dependency
      // ADDED:::: "numDependentResources" "numDependentData" "numDependentModules" "numDependentProviders"
      // ADDED:::: "numDependentProviders" "numDependentLocals" "numDependentVars"
      ImplicitResourceDependency implicitResourceDependency = new ImplicitResourceDependency();
      metrics = implicitResourceDependency.updateMetric(metrics, identifiedBlock);

      // Number of Resource Dependency
      ExplicitResourceDependency explicitResourceDependency = new ExplicitResourceDependency();
      metrics = explicitResourceDependency.updateMetric(metrics, identifiedBlock);

      // function Parameters
      // "numParams" "avgParams" "maxParams"
      FunctionParametersIdentifier functionParametersIdentifier = new FunctionParametersIdentifier();
      metrics = functionParametersIdentifier.updateMetric(metrics, identifiedBlock);

      // Here Docs
      // "numHereDocs" "avgHereDocs" "numLinesHereDocs" "avgLinesHereDocs" "maxLinesHereDocs"
      HereDocIdentifier hereDocIdentifier = new HereDocIdentifier();
      metrics = hereDocIdentifier.updateMetric(metrics, identifiedBlock);

      // Index Access
      // "numIndexAccess" "avgIndexAccess" "maxIndexAccess"
      IndexAccessIdentifier indexAccessIdentifier = new IndexAccessIdentifier();
      metrics = indexAccessIdentifier.updateMetric(metrics, identifiedBlock);

      // Literal Expressions
      // "numLiteralExpression" "numStringValues" "sumLengthStringValues" "avgLengthStringValues" "maxLengthStringValues"
      LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
      metrics = literalExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // ADDED:: numEmptyString
      EmptyStringIdentifier emptyStringIdentifier = new EmptyStringIdentifier(literalExpressionIdentifier);
      metrics = emptyStringIdentifier.updateMetric(metrics, identifiedBlock);

      // Loops Expressions
      // "numLoops" "avgLoops" "maxLoops"
      LoopsExpressionIdentifier loopsExpressionIdentifier = new LoopsExpressionIdentifier();
      metrics = loopsExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // Math Operation
      // "numMathOperations" "avgMathOperations" "maxMathOperations"
      MathOperations mathOperations = new MathOperations();
      metrics = mathOperations.updateMetric(metrics, identifiedBlock);

      // Complexity
      // "avgMccabeCC" "sumMccabeCC" "maxMccabeCC"
      MccabeCC mccabeCC = new MccabeCC();
      metrics = mccabeCC.updateMetric(metrics, identifiedBlock);

      // Meta-Arguments
      // "numMetaArg"
      MetaArgumentIdentifier metaArgumentIdentifier = new MetaArgumentIdentifier();
      metrics = metaArgumentIdentifier.updateMetrics(metrics, identifiedBlock);

      // Objects
      // "numObjects" "avgObjects" "maxTuples"
      ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();
      metrics = objectWrapperIdentifier.updateMetric(metrics, identifiedBlock);

      // Objects Elements
      // "numElemTuples" "avgElemTuples" "maxElemTuples"
      List<TerraformTreeImpl> objects = objectWrapperIdentifier.filterObjectsFromBlock(identifiedBlock);
      ObjectWrapperElementIdentifier objectWrapperElementIdentifier = new ObjectWrapperElementIdentifier(objects);
      metrics = objectWrapperElementIdentifier.updateMetric(metrics, identifiedBlock);

      // References (pointers)
      // "numReferences" "avgReferences" "maxReferences"
      ReferenceIdentifier referenceIdentifier = new ReferenceIdentifier();
      metrics = referenceIdentifier.updateMetric(metrics, identifiedBlock);

      // Variables
      // "numVars" "avgNumVars" "maxNumVars"
      VariablesIdentifier variablesIdentifier = new VariablesIdentifier();
      metrics = variablesIdentifier.updateMetric(metrics, identifiedBlock);

      // Splat Expressions
      // "numSplatExpressions" "avgSplatExpressions" "maxSplatExpressions"
      SplatExpressionIdentifier splatExpressionIdentifier = new SplatExpressionIdentifier();
      metrics = splatExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // Template Expression
      // "numTemplateExpression" "avgTemplateExpression"
      TemplateExpressionIdentifier templateExpressionIdentifier = new TemplateExpressionIdentifier();
      metrics = templateExpressionIdentifier.updateMetric(metrics, identifiedBlock);

      // Tokens
      // "textEntropyMeasure" "minAttrsTextEntropy" "maxAttrsTextEntropy" "avgAttrsTextEntropy" "numTokens"
      // "minTokensPerAttr" "maxTokensPerAttr" "avgTokensPerAttr"
      TokenIdentifier tokenIdentifier = new TokenIdentifier();
      metrics = tokenIdentifier.updateMetric(metrics, identifiedBlock);

      // Tuples
      // "numTuples" "avgTuples" "maxTuples"
      TupleIdentifier tupleIdentifier = new TupleIdentifier();
      metrics = tupleIdentifier.updateMetric(metrics, identifiedBlock);

      // Tuples Elements
      // "numElemTuples" "avgElemTuples" "maxElemTuples"
      List<TerraformTreeImpl> tuples = tupleIdentifier.filterTuplesFromBlock(identifiedBlock);
      TupleElementsIdentifier tupleElementsIdentifier = new TupleElementsIdentifier(tuples);
      metrics = tupleElementsIdentifier.updateMetric(metrics, identifiedBlock);

      // Size of Block
      // "depthOfBlock" "loc" "nloc"
      BlockComplexity blockComplexity = new BlockComplexity(identifiedBlock, blockAsString);
      metrics = blockComplexity.updateMetric(metrics, identifiedBlock);

      // Check the type of the studied block
      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();
      metrics = blockCheckerType.updateMetric(metrics, identifiedBlock);

      // Number of Attributes
      AttrFinderImpl attrFinder = new AttrFinderImpl();
      metrics = attrFinder.updateMetric(metrics, identifiedBlock);

      // Number of Deprecated Keywords
      DeprecatoryServiceLocator deprecatoryServiceLocator = new DeprecatoryServiceLocator(identifiedBlock, blockAsString);
      metrics = deprecatoryServiceLocator.updateMetrics(metrics, identifiedBlock);

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

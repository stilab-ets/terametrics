package org.stilab.utils;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.interfaces.IBlockComplexity;
import org.stilab.metrics.BlockLabelIdentifier;
import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.metrics.counter.attr.counter.AttributeCounterImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.size.BlockComplexity;
import org.stilab.metrics.counter.block_level.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MetricsCalculator {

    public MetricsCalculator() {  }



    public JSONObject measureMetrics(BlockTreeImpl identifiedBlock, String blockAsString) {

      JSONObject metrics = new JSONObject();

      // Get the name of the Impacted Block
      BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
      List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(identifiedBlock);
      metrics.put("impacted_block_type", this.concatElementsOfList(labels));

      //  1. Number of the used meta-arguments
      MetaArgumentIdentifier metaArgumentIdentifier =
        new MetaArgumentIdentifier();
      metaArgumentIdentifier.filterMetaArguments(identifiedBlock);
      int numMetaArg = metaArgumentIdentifier.meta_args_count();
      metrics.put("numMetaArg", numMetaArg);

      //  2. Number of dynamic block
      DynamicBlocksIdentifier dynamicBlocksIdentifier =
        new DynamicBlocksIdentifier();
      dynamicBlocksIdentifier.filterDynamicBlock(identifiedBlock);
      int numDynamicBlock = dynamicBlocksIdentifier.countDynamicBlock();
      metrics.put("numDynamicBlocks", numDynamicBlock);

      //  3. Number of conditions
      ConditionalExpressionIdentifier conditionalExpressionIdentifier =
        new ConditionalExpressionIdentifier();
      conditionalExpressionIdentifier.filtersConditionsFromBlock(identifiedBlock);
      int numConditions = conditionalExpressionIdentifier.totalNumberOfConditions();
      int maxConditionsPerAttr = conditionalExpressionIdentifier.maxNumberOfConditionsPerAttribute();
      double avgConditionsPerAttr = conditionalExpressionIdentifier.avgNumberOfConditionsPerAttribute();
      //      TODO: MAX, AVG, TOTAL,
      //      Should remove the TODO comment after running your tests
      //      THINK about the ELSE Template condition
      metrics.put("numConditions", numConditions);
      metrics.put("avgConditions", avgConditionsPerAttr);
      metrics.put("maxConditions",maxConditionsPerAttr);

      //  4. Number of Loops
      LoopsExpressionIdentifier loopsExpressionIdentifier =
        new LoopsExpressionIdentifier();
      loopsExpressionIdentifier.filterLoopsFromBlock(identifiedBlock);
      //      TODO: MAX, AVG, TOTAL
      //      TODO: You can here the case of count, for_each
      //      Should remove the TODO comment after running your tests
      int numLoops = loopsExpressionIdentifier.totalNumberOfLoops();
      double avgLoops = loopsExpressionIdentifier.avgNumberOfLoops();
      int maxLoops = loopsExpressionIdentifier.maxNumberOfLoops();
      metrics.put("numLoops", numLoops);
      metrics.put("avgLoops", avgLoops);
      metrics.put("maxLoops", maxLoops);


      //  5. Number of Template Expressions
      TemplateExpressionIdentifier templateExpressionIdentifier =
        new TemplateExpressionIdentifier();
      templateExpressionIdentifier.filterTemplateExpressionsFromBlock(identifiedBlock);
      int numTemplateExpression = templateExpressionIdentifier.countTemplateExpressionsPerBlock();
      metrics.put("numTemplateExpression", numTemplateExpression);

      //  6. Number of Call Methods
      FunctionCallExpressionIdentifier functionCallExpressionIdentifier =
        new FunctionCallExpressionIdentifier();
      functionCallExpressionIdentifier.filterFCfromBlock(identifiedBlock);
      //      TODO: MAX, AVG, TOTAL
      int numFunctionCall = functionCallExpressionIdentifier.totalNumberOfFunctionCall();
      double avgFunctionCall = functionCallExpressionIdentifier.avgNumberOfFunctionCall();
      int maxFunctionCall = functionCallExpressionIdentifier.maxNumberOfFunctionCall();
      metrics.put("numFunctionCall", numFunctionCall);
      metrics.put("avgFunctionCall", avgFunctionCall);
      metrics.put("maxFunctionCall", maxFunctionCall);

      //  7. Number of Parameters injected in the Call Method
      FunctionParametersIdentifier functionParametersIdentifier = new FunctionParametersIdentifier();
      functionParametersIdentifier.identifyUsedParametersInBlock(identifiedBlock);
      //      TODO: MAX, AVG, TOTAL
      int numParams = functionParametersIdentifier.totalNumberParamsPerBlock();
      double avgParams = functionParametersIdentifier.avgNumberParamsPerBlock();
      int maxParams = functionParametersIdentifier.maxNumberParamsPerBlock();
      metrics.put("numParams", numParams);
      metrics.put("avgParams", avgParams);
      metrics.put("maxParams", maxParams);

      //  8. Number of the referenced values
      ReferenceIdentifier referenceIdentifier = new ReferenceIdentifier();
      referenceIdentifier.filterAttributeAccessFromBlock(identifiedBlock);
      //      TODO: MAX, AVG, TOTAL
      int numReferences = referenceIdentifier.totalAttributeAccess();
      double avgReferences = referenceIdentifier.avgAttributeAccess();
      int maxReferences = referenceIdentifier.maxAttributeAccess();
      metrics.put("numReferences", numReferences);
      metrics.put("avgReferences", avgReferences);
      metrics.put("maxReferences", maxReferences);

      //  9. Number of splat expressions
      SplatExpressionIdentifier splatExpressionIdentifier = new SplatExpressionIdentifier();
      splatExpressionIdentifier.filtersSplatsFromBlock(identifiedBlock);

      //      TODO: MAX, AVG, TOTAL
      int numSplatExpressions = splatExpressionIdentifier.totalSplatExpressions();
      double avgSplatExpressions = splatExpressionIdentifier.avgSplatExpressions();
      int maxSplatExpressions = splatExpressionIdentifier.maxSplatExpressions();
      metrics.put("numSplatExpressions", numSplatExpressions);
      metrics.put("avgSplatExpressions", avgSplatExpressions);
      metrics.put("maxSplatExpressions", maxSplatExpressions);

      // 10. Number of Index Access Identifier Expressions
      IndexAccessIdentifier indexAccessIdentifier = new IndexAccessIdentifier();
      indexAccessIdentifier.identifyIndexAccessFromBlock(identifiedBlock);
      //      TODO: MAX, AVG, TOTAL
      int numIndexAccessExpressions = indexAccessIdentifier.totalIndexAccessExpressions();
      double avgIndexAccessExpressions = indexAccessIdentifier.avgIndexAccessExpressions();
      int maxIndexAccessExpressions = indexAccessIdentifier.maxIndexAccessExpressions();
      metrics.put("numIndexAccess", numIndexAccessExpressions);
      metrics.put("avgIndexAccess", avgIndexAccessExpressions);
      metrics.put("maxIndexAccess", maxIndexAccessExpressions);

      //  10. Depth of Block
      IBlockComplexity blockComplexity = new BlockComplexity(identifiedBlock, blockAsString);
      int depthOfBlock = blockComplexity.depthOfBlock();
      metrics.put("depthOfBlock", depthOfBlock);

      //  11. Number of lines of code
      int loc = blockComplexity.LOC();
      metrics.put("loc", loc);

      //  12. Number of Non-line of code ( comments + blank)
      int nloc = blockComplexity.NLOC();
      metrics.put("nloc", nloc);

      //  13. Number of nested Block
      NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
      nestedBlockCounter.identifyNestedBlock(identifiedBlock);
      int numNestedBlocks = nestedBlockCounter.countNestedBlock();
      metrics.put("numNestedBlocks", numNestedBlocks);

      //  16. Number of attributes [Top + Nested]
      AttributeCounterImpl attributeCounter = new AttributeCounterImpl();
      int attributes = attributeCounter.countAttribute(identifiedBlock);
      metrics.put("numAttributes", attributes);

      //  17. Number of decisions
      DecisionsIdentifier decisionsIdentifier = new DecisionsIdentifier();
      decisionsIdentifier.identifyDecisionsOperators(identifiedBlock);
      int numDecisions = decisionsIdentifier.countDecisions();
      metrics.put("numDecisions", numDecisions);
//      TODO: MAX, AVG, TOTAL

      //  18. Number of Math Operations
      MathOperations mathOperations = new MathOperations();
      mathOperations.identifyMathOperators(identifiedBlock);
      int numMathOperations = mathOperations.countMathOperation();
      metrics.put("numMathOperations", numMathOperations);

      // 19. TextEntropy
      TokenIdentifier tokenIdentifier = new TokenIdentifier();
      TextEntropy textEntropy = new TextEntropy();
      List<Character> characters = tokenIdentifier
        .textualize(identifiedBlock);
      Map<Character, Integer> characterFrequency = textEntropy
        .countCharacterFrequency(characters);
      double textEntropyMeasure = textEntropy
        .textEntropy(characterFrequency);
      metrics.put("textEntropyMeasure", textEntropyMeasure);

      //  20. Number of regex expressions
      RegexIdentifier regexIdentifier = new RegexIdentifier();
      regexIdentifier.identifyRegexOperators(identifiedBlock);
      int numRegex = regexIdentifier.countRegex();
      metrics.put("numRegex", numRegex);

      //  21. Number of Commands
      CommandIdentifier commandIdentifier = new CommandIdentifier();
      commandIdentifier.identifyCommandsFromBlock(identifiedBlock);
      int numCommand = commandIdentifier.countCommandsPerBlock();
      metrics.put("numCommand", numCommand);

      //  22. Numbers of Urls
      URIsIdentifier urIsIdentifier = new URIsIdentifier();
      int numUris = urIsIdentifier.countUri(blockAsString);
      metrics.put("numUris", numUris);

      //  29. Avg Mccab complexity --- Mascara
      MccabeCC mccabeCC = new MccabeCC();
      double avgMccabeCC = mccabeCC.avgMccabeCC(identifiedBlock);
      double sumMccabeCC = mccabeCC.sumMccabeCC(identifiedBlock);
      metrics.put("avgMccabeCC", avgMccabeCC);
      metrics.put("sumMccabeCC", sumMccabeCC);

      // 30. Key of the block <==> Variable, Provider, Module, Local, Output, Data
      // boolean isResource = blockCheckerType.isResource(identifiedBlock);
      metrics.put("block", identifiedBlock.key().value());

      // 31. Start index
      metrics.put("start_block", identifiedBlock.key().textRange().start().line());

      // 32. End index
      metrics.put("end_block", identifiedBlock.value().textRange().end().line());

      // 33. Identifier of a block
      labels.add(0, identifiedBlock.key().value());
      metrics.put("block_identifiers", this.concatElementsOfList(labels));

      // 34. Number of literal Expression
      LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
      int numLiteralExpressions = literalExpressionIdentifier.filterLiteralExprFromBlock(identifiedBlock).size();
      metrics.put("numLiteralExpression", numLiteralExpressions);

      // 35. Number of Tuple Expression
      TupleIdentifier tupleIdentifier = new TupleIdentifier();
      List<TerraformTreeImpl> tuples = tupleIdentifier.filterTuplesFromBlock(identifiedBlock);
//      TODO: MAX, AVG, TOTAL
      int numTuples = tupleIdentifier.totalNumberOfTuples();
      double avgTuples = tupleIdentifier.avgNumberOfTuples();
      int maxTuples = tupleIdentifier.maxNumberOfTuples();
      metrics.put("numTuples", numTuples);
      metrics.put("avgTuples", avgTuples);
      metrics.put("maxTuples", maxTuples);

//      Number of Tuple Elements
      TupleElementsIdentifier tupleElementsIdentifier = new TupleElementsIdentifier(tuples);

//      TODO: MAX, AVG, TOTAL
      int numElemTuples = tupleElementsIdentifier.getTotalNumberOfElementsOfDifferentTuples();
      double avgElemTuples = tupleElementsIdentifier.avgNumberOfElementsPerDifferentTuples();
      int maxElemTuples = tupleElementsIdentifier.maxNumberOfElementsPerDifferentTuples();
      metrics.put("numElemTuples", numElemTuples);
      metrics.put("avgElemTuples", avgElemTuples);
      metrics.put("maxElemTuples", maxElemTuples);

//    Number of HereDoc
      HereDocIdentifier hereDocIdentifier = new HereDocIdentifier();
      hereDocIdentifier.filterHereDocsFromBlock(identifiedBlock);
//      TODO: ADD the HereDoc: AVG, TOTAL, MAX
      int numHereDocs = hereDocIdentifier.totalNumberOfHereDoc();
      double avgLinesHereDocs = hereDocIdentifier.avgNumberLinesPerHereDoc();
      int maxLinesHereDocs = hereDocIdentifier.maxNumberLinesPerHereDoc();
      metrics.put("numHereDocs", numHereDocs);
      metrics.put("avgLinesHereDocs", avgLinesHereDocs);
      metrics.put("maxLinesHereDocs", maxLinesHereDocs);

//     Number of Objects
      ObjectWrapperIdentifier objectWrapperIdentifier = new ObjectWrapperIdentifier();
      List<TerraformTreeImpl> objects = objectWrapperIdentifier.filterObjectsFromBlock(identifiedBlock);
//      TODO: ADD the ObjectWrapper: AVG, TOTAL, MAX
      int numObjects = objectWrapperIdentifier.totalNumberOfObjects();
      double avgObjects = objectWrapperIdentifier.avgNumberOfObjects();
      int maxObjects = objectWrapperIdentifier.maxNumberOfObjects();
      metrics.put("numObjects", numObjects);
      metrics.put("avgObjects", avgObjects);
      metrics.put("maxTuples", maxObjects);

//      Number Of elements Within Objects
      ObjectWrapperElementIdentifier objectWrapperElementIdentifier = new ObjectWrapperElementIdentifier(objects);
      int numElementObjects = objectWrapperElementIdentifier.getTotalNumberOfElementsOfDifferentObjects();
      double avgElementObjects = objectWrapperElementIdentifier.avgNumberOfElementsPerDifferentObjects();
      int maxElementObjects = objectWrapperElementIdentifier.maxNumberOfElementsPerDifferentObjects();

      metrics.put("numElemTuples", numElementObjects);
      metrics.put("avgElemTuples", avgElementObjects);
      metrics.put("maxElemTuples", maxElementObjects);


//      TODO: ADD the VAR IDENTIFIER: AVG, TOTAL, MAX
      VariablesIdentifier variablesIdentifier = new VariablesIdentifier();
      int numVars = variablesIdentifier.totalNumberOfVars();
      double avgNumVars = variablesIdentifier.avgNumberOfVars();
      int maxNumVars = variablesIdentifier.maxNumberOfVars();

      metrics.put("numVars", numVars);
      metrics.put("avgNumVars", avgNumVars);
      metrics.put("maxNumVars", maxNumVars);

//    Check the type of the studied block
      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();
      metrics = blockCheckerType.checkBlockType(identifiedBlock, metrics);



      return metrics;
    }


    public String concatElementsOfList(List<String> stringList) {
       return String.join(" ", stringList);
    }

    public void writeMetricsAsJsonFile(BlockTreeImpl blockTree,
                                       String blockAsString,
                                       String filePath) {
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

    public void createEmptyFile(String filePath) {
      try {
        File file = new File(filePath);
        if (file.createNewFile()) {
          System.out.println("Empty file created: " + file.getAbsolutePath());
        } else {
          System.out.println("File already exists: " + file.getAbsolutePath());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


}

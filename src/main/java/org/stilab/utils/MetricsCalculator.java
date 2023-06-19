package org.stilab.utils;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.interfaces.IBlockComplexity;
import org.stilab.metrics.BlockLabelIdentifier;
import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.metrics.counter.attr.counter.AttributeCounterImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.size.BlockComplexity;
import org.stilab.metrics.counter.expression.*;

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
      List<String> identifierBlack = blockLabelIdentifier.identifyLabelsOfBlock(identifiedBlock);
      metrics.put("impacted_block_type", identifierBlack.toString());

      //  1. Number of the used meta-arguments
      MetaArgumentIdentifier metaArgumentIdentifier =
        new MetaArgumentIdentifier();
      metaArgumentIdentifier.filterMetaArguments(identifiedBlock);
      int numMetaArg = metaArgumentIdentifier.meta_args_count();
      metrics.put("numMetaArg", numMetaArg);

      //  2. Number od dynamic block
      DynamicBlocksIdentifier dynamicBlocksIdentifier =
        new DynamicBlocksIdentifier();
      dynamicBlocksIdentifier.filterDynamicBlock(identifiedBlock);
      int numDynamicBlock = dynamicBlocksIdentifier.countDynamicBlock();
      metrics.put("numDynamicBlock", numDynamicBlock);

      //  3. Number of conditions
      ConditionalExpressionIdentifier conditionalExpressionIdentifier =
        new ConditionalExpressionIdentifier();
      conditionalExpressionIdentifier.filtersConditionsFromBlock(identifiedBlock);
      int numConditions = conditionalExpressionIdentifier.countConditions();
      metrics.put("numConditions", numConditions);

      //  4. Number of Loops
      LoopsExpressionIdentifier loopsExpressionIdentifier =
        new LoopsExpressionIdentifier();
      loopsExpressionIdentifier.filterLoopsFromBlock(identifiedBlock);
      int numLoops = loopsExpressionIdentifier.countLoop();
      metrics.put("numLoops", numLoops);

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
      int numFunctionCallParameters = functionCallExpressionIdentifier.countFunctionCallsPerBlock();
      metrics.put("numFunctionCallParameters", numFunctionCallParameters);

      //  7. Number of Parameters injected in the Call Method
      FunctionParametersIdentifier functionParametersIdentifier =
        new FunctionParametersIdentifier();
      functionParametersIdentifier.identifyUsedParametersInBlock(identifiedBlock);
      int numFunctionParameters = functionParametersIdentifier.countParametersPerBlock();
      metrics.put("numFunctionParameters", numFunctionParameters);

      //  8. Number of the referenced values
      ReferenceIdentifier referenceIdentifier = new ReferenceIdentifier();
      referenceIdentifier.filterAttributeAccessFromBlock(identifiedBlock);
      int numReferences = referenceIdentifier.countAttributeAccessPerBlock();
      metrics.put("numReferences", numReferences);

      //  9. Number of splat expressions
      SplatExpressionIdentifier splatExpressionIdentifier = new SplatExpressionIdentifier();
      splatExpressionIdentifier.filtersConditionsFromBlock(identifiedBlock);
      int numSplatExpressions = splatExpressionIdentifier.countSplats();
      metrics.put("numSplatExpressions", numSplatExpressions);

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

      //  18. Number of Math Operations
      MathOperations mathOperations = new MathOperations();
      mathOperations.identifyMathOperators(identifiedBlock);
      int numMathOperations = mathOperations.countDecisions();
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

      //  23. Is a Data Block
      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();
      boolean isData = blockCheckerType.isData(identifiedBlock);
      metrics.put("isData", isData);

      //  24. Is a Variable Block
      boolean isVar = blockCheckerType.isVariable(identifiedBlock);
      metrics.put("isVar", isVar);

      //  25. Is a Provider Block
      boolean isProvider = blockCheckerType.isProvider(identifiedBlock);
      metrics.put("isProvider", isProvider);

      //  26. Is a Module Block
      boolean isModule = blockCheckerType.isModule(identifiedBlock);
      metrics.put("isModule", isModule);

      //  27. Is a Local Block
      boolean isLocal = blockCheckerType.isLocals(identifiedBlock);
      metrics.put("isLocal", isLocal);

      //  28. Is an Output Block
      boolean isOutput = blockCheckerType.isOutput(identifiedBlock);
      metrics.put("isOutput", isOutput);

      //  29. Avg Mccab complexity --- Mascara
      MccabeCC mccabeCC = new MccabeCC();
      double avgMccabeCC = mccabeCC.avgMccabeCC(identifiedBlock);
      metrics.put("avgMccabeCC", avgMccabeCC);

      return metrics;
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

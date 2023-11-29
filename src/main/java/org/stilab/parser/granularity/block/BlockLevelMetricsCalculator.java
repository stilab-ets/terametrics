package org.stilab.parser.granularity.block;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.collectors.*;

import java.util.Arrays;
import java.util.List;

public class BlockLevelMetricsCalculator {

    public JSONObject measureMetrics(BlockTreeImpl identifiedBlock, String blockAsString) {

      JSONObject metrics = new JSONObject();

      List<Decorator> repositories = Arrays.asList(
        new BlockComplexityCollector(blockAsString),
        new BlockMetaInfoCollector(),
        new ComparisonOperatorsCollector(),
        new ConditionalExpressionCollector(),
        new DebuggingFunctionCollector(),
        new DeprecatedFunctionsCollector(),
        new DynamicBlocksCollector(),
        new ExplicitResourceDependencyCollector(),
        new FunctionCallExpressionCollector(),
        new FunctionParametersCollector(),
        new HereDocCollector(),
        new ImplicitResourceCollector(),
        new IndexAccessCollector(),
        new LiteralExpressionCollector(),
        new LogicalOperationsCollector(),
        new LookUpFunctionCollector(),
        new LoopsExpressionCollector(),
        new MathOperationsCollector(),
        new MccabeCCCollector(),
        new MetaArgumentCollector(),
        new NestedBlockCollector(),
        new ObjectWrapperCollector(),
        new ObjectWrapperElementCollector(),
        new ReferenceCollector(),
        new SpecialStringCollector(),
        new SplatExpressionCollector(),
        new TemplateExpressionCollector(),
        new TokenCollector(),
        new TupleCollector(),
        new TupleElementsCollector(),
        new VariablesCollector(),
        new BlockCheckTypeCollector(),
        new AttributesCollector()
      );

      for (Decorator decorator : repositories) {
         decorator.decorateMetric(metrics, identifiedBlock);
      }


      return metrics;
    }

}

package org.stilab.metrics.granularity.block;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.data.repository.*;

import java.util.Arrays;
import java.util.List;

public class BlockLevelMetricsCalculator {

    public JSONObject measureMetrics(BlockTreeImpl identifiedBlock, String blockAsString) {

      JSONObject metrics = new JSONObject();

      List<Repository> repositories = Arrays.asList(
        new BlockComplexityRepository(blockAsString),
        new BlockMetaInfoRepository(),
        new ComparisonOperatorsRepository(),
        new ConditionalExpressionRepository(),
        new DebuggingFunctionRepository(),
        new DeprecatedFunctionsRepository(),
        new DynamicBlocksRepository(),
        new ExplicitResourceDependencyRepository(),
        new FunctionCallExpressionRepository(),
        new FunctionParametersRepository(),
        new HereDocRepository(),
        new ImplicitResourceRepository(),
        new IndexAccessRepository(),
        new LiteralExpressionRepository(),
        new LogicalOperationsRepository(),
        new LookUpFunctionRepository(),
        new LoopsExpressionRepository(),
        new MathOperationsRepository(),
        new MccabeCCRepository(),
        new MetaArgumentRepository(),
        new NestedBlockRepository(),
        new ObjectWrapperRepository(),
        new ObjectWrapperElementRepository(),
        new ReferenceRepository(),
        new SpecialStringRepository(),
        new SplatExpressionRepository(),
        new TemplateExpressionRepository(),
        new TokenRepository(),
        new TupleRepository(),
        new TupleElementsRepository(),
        new TupleRepository(),
        new VariablesRepository(),
        new BlockCheckTypeRepository(),
        new AttributesRepository()
      );

      for (Repository repository: repositories) {
         repository.updateMetric(metrics, identifiedBlock);
      }


      return metrics;
    }

}

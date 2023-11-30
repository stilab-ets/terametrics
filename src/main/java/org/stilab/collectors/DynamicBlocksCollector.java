package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.DynamicBlocksCalculator;
import org.stilab.visitors.DynamicBlocksVisitor;

public class DynamicBlocksCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      DynamicBlocksCalculator dynamicBlocksCalculator = new DynamicBlocksCalculator(identifiedBlock);
      metrics.put("numDynamicBlocks", dynamicBlocksCalculator.countDynamicBlocks());
      return metrics;

    }
}

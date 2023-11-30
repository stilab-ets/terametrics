package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.calculators.TupleElementsCalculator;
import org.stilab.visitors.TupleElementsVisitor;
import org.stilab.visitors.TupleVisitor;

import java.util.List;

public class TupleElementsCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TupleElementsCalculator tupleElementsCalculator = new TupleElementsCalculator(identifiedBlock);

      metrics.put("numElemTuples", tupleElementsCalculator.getTotalNumberOfElementsOfDifferentTuples());
      metrics.put("avgElemTuples", tupleElementsCalculator.avgNumberOfElementsPerDifferentTuples());
      metrics.put("maxElemTuples", tupleElementsCalculator.maxNumberOfElementsPerDifferentTuples());

      return metrics;
    }
}

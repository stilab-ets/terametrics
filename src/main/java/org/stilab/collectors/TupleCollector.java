package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.TupleCalculator;
import org.stilab.visitors.TupleVisitor;

public class TupleCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

//      TupleVisitor tupleVisitor = new TupleVisitor();
//
//      tupleVisitor.filterTuplesFromBlock(identifiedBlock);


      TupleCalculator tupleCalculator = new TupleCalculator(identifiedBlock);
      int numTuples = tupleCalculator.totalNumberOfTuples();
      double avgTuples = tupleCalculator.avgNumberOfTuples();
      int maxTuples = tupleCalculator.maxNumberOfTuples();


      metrics.put("numTuples", numTuples);
      metrics.put("avgTuples", avgTuples);
      metrics.put("maxTuples", maxTuples);

      return metrics;
    }

}

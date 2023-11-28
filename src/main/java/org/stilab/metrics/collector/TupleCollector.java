package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.visitors.TupleVisitor;

public class TupleCollector implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TupleVisitor tupleVisitor = new TupleVisitor();

      tupleVisitor.filterTuplesFromBlock(identifiedBlock);
      int numTuples = tupleVisitor.totalNumberOfTuples();
      double avgTuples = tupleVisitor.avgNumberOfTuples();
      int maxTuples = tupleVisitor.maxNumberOfTuples();

      metrics.put("numTuples", numTuples);
      metrics.put("avgTuples", avgTuples);
      metrics.put("maxTuples", maxTuples);

      return metrics;
    }

}

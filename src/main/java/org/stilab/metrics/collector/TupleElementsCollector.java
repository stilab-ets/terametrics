package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.visitors.TupleElementsVisitor;
import org.stilab.metrics.visitors.TupleVisitor;

import java.util.List;

public class TupleElementsCollector implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TupleVisitor tupleVisitor = new TupleVisitor();
      List<TerraformTreeImpl> tuples = tupleVisitor.filterTuplesFromBlock(identifiedBlock);

      TupleElementsVisitor tupleElementsVisitor = new TupleElementsVisitor(tuples);

      int numElemTuples = tupleElementsVisitor.getTotalNumberOfElementsOfDifferentTuples();
      double avgElemTuples = tupleElementsVisitor.avgNumberOfElementsPerDifferentTuples();
      int maxElemTuples = tupleElementsVisitor.maxNumberOfElementsPerDifferentTuples();

      metrics.put("numElemTuples", numElemTuples);
      metrics.put("avgElemTuples", avgElemTuples);
      metrics.put("maxElemTuples", maxElemTuples);

      return metrics;
    }
}

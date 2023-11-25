package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.counter.block.visitors.TupleElementsVisitor;

import java.util.List;

public class TupleElementsRepository implements Repository {


  private List<TerraformTreeImpl> tupleTrees;

  public TupleElementsRepository(List<TerraformTreeImpl> tupleTrees){
    this.tupleTrees = tupleTrees;
  }

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      TupleElementsVisitor tupleElementsVisitor = new TupleElementsVisitor(this.tupleTrees);

      int numElemTuples = tupleElementsVisitor.getTotalNumberOfElementsOfDifferentTuples();
      double avgElemTuples = tupleElementsVisitor.avgNumberOfElementsPerDifferentTuples();
      int maxElemTuples = tupleElementsVisitor.maxNumberOfElementsPerDifferentTuples();

      metrics.put("numElemTuples", numElemTuples);
      metrics.put("avgElemTuples", avgElemTuples);
      metrics.put("maxElemTuples", maxElemTuples);

      return metrics;
    }
}

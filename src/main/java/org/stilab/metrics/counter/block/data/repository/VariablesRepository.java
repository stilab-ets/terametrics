package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.VariablesVisitor;

public class VariablesRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      VariablesVisitor variablesVisitor = new VariablesVisitor();

      variablesVisitor.filterVarsFromBlock(identifiedBlock);
      int numVars = variablesVisitor.totalNumberOfVars();
      double avgNumVars = variablesVisitor.avgNumberOfVars();
      int maxNumVars = variablesVisitor.maxNumberOfVars();

      metrics.put("numVars", numVars);
      metrics.put("avgNumVars", avgNumVars);
      metrics.put("maxNumVars", maxNumVars);

      return metrics;
    }
}

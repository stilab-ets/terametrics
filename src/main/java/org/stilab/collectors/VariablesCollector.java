package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.VariablesCalculator;

public class VariablesCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      VariablesCalculator variablesCalculator = new VariablesCalculator(identifiedBlock);

      int numVars = variablesCalculator.totalNumberOfVars();
      double avgNumVars = variablesCalculator.avgNumberOfVars();
      int maxNumVars = variablesCalculator.maxNumberOfVars();

      metrics.put("numVars", numVars);
      metrics.put("avgNumVars", avgNumVars);
      metrics.put("maxNumVars", maxNumVars);

      return metrics;
    }
}

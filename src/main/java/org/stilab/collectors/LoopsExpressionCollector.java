package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.LoopsExpressionCalculator;
import org.stilab.visitors.LoopsExpressionVisitor;

public class LoopsExpressionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      LoopsExpressionCalculator loopsExpressionCalculator = new LoopsExpressionCalculator(identifiedBlock);

      int numLoops = loopsExpressionCalculator.totalNumberOfLoops();
      double avgLoops = loopsExpressionCalculator.avgNumberOfLoops();
      int maxLoops = loopsExpressionCalculator.maxNumberOfLoops();

      metrics.put("numLoops", numLoops);
      metrics.put("avgLoops", avgLoops);
      metrics.put("maxLoops", maxLoops);

      return metrics;
    }

}

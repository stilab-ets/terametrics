package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.LoopsExpressionVisitor;

public class LoopsExpressionCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      LoopsExpressionVisitor loopsExpressionVisitor = new LoopsExpressionVisitor();


      loopsExpressionVisitor.filterLoopsFromBlock(identifiedBlock);
      int numLoops = loopsExpressionVisitor.totalNumberOfLoops();
      double avgLoops = loopsExpressionVisitor.avgNumberOfLoops();
      int maxLoops = loopsExpressionVisitor.maxNumberOfLoops();

      metrics.put("numLoops", numLoops);
      metrics.put("avgLoops", avgLoops);
      metrics.put("maxLoops", maxLoops);

      return metrics;
    }

}

package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.visitors.LoopsExpressionVisitor;

public class LoopsExpressionRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

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

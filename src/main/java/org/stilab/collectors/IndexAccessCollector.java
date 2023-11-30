package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.IndexAccessCalculator;
import org.stilab.visitors.IndexAccessVisitor;

public class IndexAccessCollector implements Decorator {

      @Override
      public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

//        IndexAccessVisitor indexAccessVisitor = new IndexAccessVisitor();
//        indexAccessVisitor.identifyIndexAccessFromBlock(identifiedBlock);

        IndexAccessCalculator indexAccessCalculator = new IndexAccessCalculator(identifiedBlock);
        int numIndexAccessExpressions = indexAccessCalculator.totalIndexAccessExpressions();
        double avgIndexAccessExpressions = indexAccessCalculator.avgIndexAccessExpressions();
        int maxIndexAccessExpressions = indexAccessCalculator.maxIndexAccessExpressions();

        metrics.put("numIndexAccess", numIndexAccessExpressions);
        metrics.put("avgIndexAccess", avgIndexAccessExpressions);
        metrics.put("maxIndexAccess", maxIndexAccessExpressions);
        return metrics;

      }
}

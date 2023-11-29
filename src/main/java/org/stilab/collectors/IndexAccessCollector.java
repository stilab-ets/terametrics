package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.IndexAccessVisitor;

public class IndexAccessCollector implements Decorator {

      @Override
      public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

        IndexAccessVisitor indexAccessVisitor = new IndexAccessVisitor();

        indexAccessVisitor.identifyIndexAccessFromBlock(identifiedBlock);
        int numIndexAccessExpressions = indexAccessVisitor.totalIndexAccessExpressions();
        double avgIndexAccessExpressions = indexAccessVisitor.avgIndexAccessExpressions();
        int maxIndexAccessExpressions = indexAccessVisitor.maxIndexAccessExpressions();

        metrics.put("numIndexAccess", numIndexAccessExpressions);
        metrics.put("avgIndexAccess", avgIndexAccessExpressions);
        metrics.put("maxIndexAccess", maxIndexAccessExpressions);
        return metrics;

      }
}

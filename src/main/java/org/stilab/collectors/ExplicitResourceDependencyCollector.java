package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ExplicitResourceDependencyVisitor;

public class ExplicitResourceDependencyCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      ExplicitResourceDependencyVisitor explicitResourceDependencyVisitor = new ExplicitResourceDependencyVisitor();
      metrics.put("numExplicitResourceDependency", explicitResourceDependencyVisitor.getNumberOfResourceDependency(identifiedBlock));
      return metrics;
    }
}

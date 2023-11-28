package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.ExplicitResourceDependencyVisitor;

public class ExplicitResourceDependencyRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      ExplicitResourceDependencyVisitor explicitResourceDependencyVisitor = new ExplicitResourceDependencyVisitor();
      metrics.put("numExplicitResourceDependency", explicitResourceDependencyVisitor.getNumberOfResourceDependency(identifiedBlock));
      return metrics;
    }
}

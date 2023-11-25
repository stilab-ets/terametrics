package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.ExplicitResourceDependencyVisitor;

public class ExplicitResourceDependencyRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      ExplicitResourceDependencyVisitor explicitResourceDependencyVisitor = new ExplicitResourceDependencyVisitor();
      metrics.put("numExplicitResourceDependency", explicitResourceDependencyVisitor.getNumberOfResourceDependency(identifiedBlock));
      return metrics;
    }
}

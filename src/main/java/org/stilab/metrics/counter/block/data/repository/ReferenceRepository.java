package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.visitors.ReferenceVisitor;

public class ReferenceRepository implements Repository {


    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ReferenceVisitor referenceVisitor = new ReferenceVisitor();

      referenceVisitor.filterAttributeAccessFromBlock(identifiedBlock);
      int numReferences = referenceVisitor.totalAttributeAccess();
      double avgReferences = referenceVisitor.avgAttributeAccess();
      int maxReferences = referenceVisitor.maxAttributeAccess();

      metrics.put("numReferences", numReferences);
      metrics.put("avgReferences", avgReferences);
      metrics.put("maxReferences", maxReferences);

      return metrics;
    }
}

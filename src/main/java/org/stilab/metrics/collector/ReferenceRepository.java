package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.visitors.ReferenceVisitor;

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

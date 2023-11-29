package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.ReferenceVisitor;

public class ReferenceCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

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

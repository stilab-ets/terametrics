package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.ReferenceCalculator;
import org.stilab.visitors.ReferenceVisitor;

public class ReferenceCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ReferenceCalculator referenceCalculator = new ReferenceCalculator(identifiedBlock);

      int numReferences = referenceCalculator.totalAttributeAccess();
      double avgReferences = referenceCalculator.avgAttributeAccess();
      int maxReferences = referenceCalculator.maxAttributeAccess();

      metrics.put("numReferences", numReferences);
      metrics.put("avgReferences", avgReferences);
      metrics.put("maxReferences", maxReferences);

      return metrics;
    }
}

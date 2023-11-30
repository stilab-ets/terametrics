package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.calculators.ObjectWrapperCalculator;
import org.stilab.visitors.ObjectWrapperVisitor;

public class ObjectWrapperCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ObjectWrapperCalculator objectWrapperCalculator = new ObjectWrapperCalculator(identifiedBlock);

      int numObjects = objectWrapperCalculator.totalNumberOfObjects();
      double avgObjects = objectWrapperCalculator.avgNumberOfObjects();
      int maxObjects = objectWrapperCalculator.maxNumberOfObjects();

      metrics.put("numObjects", numObjects);
      metrics.put("avgObjects", avgObjects);
      metrics.put("maxObjects", maxObjects);

      return metrics;
    }
}

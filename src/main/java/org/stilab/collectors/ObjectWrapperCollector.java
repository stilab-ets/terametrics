package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.ObjectWrapperVisitor;

public class ObjectWrapperCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ObjectWrapperVisitor objectWrapperVisitor = new ObjectWrapperVisitor();

      objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);
      int numObjects = objectWrapperVisitor.totalNumberOfObjects();
      double avgObjects = objectWrapperVisitor.avgNumberOfObjects();
      int maxObjects = objectWrapperVisitor.maxNumberOfObjects();

      metrics.put("numObjects", numObjects);
      metrics.put("avgObjects", avgObjects);
      metrics.put("maxObjects", maxObjects);

      return metrics;
    }
}

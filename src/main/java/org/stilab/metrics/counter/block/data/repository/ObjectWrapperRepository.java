package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.visitors.ObjectWrapperVisitor;

public class ObjectWrapperRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

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

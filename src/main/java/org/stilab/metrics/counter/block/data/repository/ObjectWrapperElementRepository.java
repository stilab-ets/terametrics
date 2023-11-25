package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.counter.block.visitors.ObjectWrapperElementVisitor;

import java.util.List;

public class ObjectWrapperElementRepository implements Repository {

    private List<TerraformTreeImpl> objects;

    public ObjectWrapperElementRepository(List<TerraformTreeImpl> objects) {
      this.objects = objects;
    }

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      ObjectWrapperElementVisitor objectWrapperElementVisitor = new ObjectWrapperElementVisitor(this.objects);

      int numElementObjects = objectWrapperElementVisitor.getTotalNumberOfElementsOfDifferentObjects();
      double avgElementObjects = objectWrapperElementVisitor.avgNumberOfElementsPerDifferentObjects();
      int maxElementObjects = objectWrapperElementVisitor.maxNumberOfElementsPerDifferentObjects();
      metrics.put("numElemObjects", numElementObjects);
      metrics.put("avgElemObjects", avgElementObjects);
      metrics.put("maxElemObjects", maxElementObjects);
      return metrics;
    }

}

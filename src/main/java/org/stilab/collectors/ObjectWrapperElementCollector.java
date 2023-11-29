package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.visitors.ObjectWrapperElementVisitor;
import org.stilab.visitors.ObjectWrapperVisitor;

import java.util.List;

public class ObjectWrapperElementCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){


      ObjectWrapperVisitor objectWrapperVisitor = new ObjectWrapperVisitor();
      List<TerraformTreeImpl> objects = objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);

      ObjectWrapperElementVisitor objectWrapperElementVisitor = new ObjectWrapperElementVisitor(objects);

      int numElementObjects = objectWrapperElementVisitor.getTotalNumberOfElementsOfDifferentObjects();
      double avgElementObjects = objectWrapperElementVisitor.avgNumberOfElementsPerDifferentObjects();
      int maxElementObjects = objectWrapperElementVisitor.maxNumberOfElementsPerDifferentObjects();

      metrics.put("numElemObjects", numElementObjects);
      metrics.put("avgElemObjects", avgElementObjects);
      metrics.put("maxElemObjects", maxElementObjects);

      return metrics;
    }

}

package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.visitors.ObjectWrapperElementVisitor;
import org.stilab.visitors.ObjectWrapperVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ObjectWrapperElementCalculator {

  private ObjectWrapperElementVisitor objectWrapperElementVisitor;

  public ObjectWrapperElementCalculator(BlockTreeImpl identifiedBlock) {

    ObjectWrapperVisitor objectWrapperVisitor = new ObjectWrapperVisitor();
    List<TerraformTreeImpl> objects = objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);
    objectWrapperElementVisitor = new ObjectWrapperElementVisitor(objects);

  }

  public int getTotalNumberOfElementsOfDifferentObjects() {
    return objectWrapperElementVisitor.getTotalNumberOfElementsOfDifferentObjects();
  }

  public double avgNumberOfElementsPerDifferentObjects() {
    if (!objectWrapperElementVisitor.getObjects().isEmpty()) {
      double avgNumberOfElementsPerDifferentObjects = (double) objectWrapperElementVisitor.getTotalNumberOfElementsOfDifferentObjects() / objectWrapperElementVisitor.getObjects().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfElementsPerDifferentObjects() {

    List<ObjectTreeImpl> localObjects = objectWrapperElementVisitor.filterOnlyObjectTreeImpl();
    int max = 0;

    if (!localObjects.isEmpty()) {
      max = objectWrapperElementVisitor.getNumberElementsContainedInAnObject(localObjects.get(0));
      for (ObjectTreeImpl objectTree: localObjects) {
        int tmpValue = objectWrapperElementVisitor.getNumberElementsContainedInAnObject(objectTree);
        if (max < tmpValue) {
          max = tmpValue;
        }
      }
    }

    if (max == 0) {
      List<ForObjectTreeImpl> forTuples = objectWrapperElementVisitor.filterOnlyForObjectTreeImpl();
      if (!forTuples.isEmpty()) {
        max = 1;
      }
    }
    return max;
  }

}

package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.ForObjectTreeImpl;
import org.sonar.iac.terraform.tree.impl.ObjectTreeImpl;
import org.stilab.visitors.ObjectWrapperVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ObjectWrapperCalculator {

  private ObjectWrapperVisitor objectWrapperVisitor;

  public ObjectWrapperCalculator(BlockTreeImpl identifiedBlock){
    objectWrapperVisitor = new ObjectWrapperVisitor();
    objectWrapperVisitor.filterObjectsFromBlock(identifiedBlock);
  }

  public int totalNumberOfObjects() {
    return objectWrapperVisitor.getObjects().size();
  }

  public double avgNumberOfObjects() {
    if (!objectWrapperVisitor.getAttributes().isEmpty()) {
      double avgNumberOfObjects = (double) totalNumberOfObjects() / objectWrapperVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfObjects).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfObjects() {

    if (objectWrapperVisitor.getAttributes().isEmpty()) { return 0;}
    int max = objectWrapperVisitor.visit(objectWrapperVisitor.getAttributes().get(0)).size();
    for (AttributeTreeImpl attribute: objectWrapperVisitor.getAttributes()) {
      int value = objectWrapperVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;

  }

}

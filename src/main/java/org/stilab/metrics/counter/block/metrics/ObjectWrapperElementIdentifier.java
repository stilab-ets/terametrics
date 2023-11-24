package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.ForObjectTreeImpl;
import org.sonar.iac.terraform.tree.impl.ObjectTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectWrapperElementIdentifier {

  List<TerraformTreeImpl> objects = new ArrayList<>();

  public ObjectWrapperElementIdentifier(List<TerraformTreeImpl> objects) {
        this.objects = objects;
  }

  public List<ObjectTreeImpl> filterOnlyObjectTreeImpl() {

    return objects.stream()
      .filter(child -> child instanceof ObjectTreeImpl)
      .map(child -> (ObjectTreeImpl) child )
      .collect(Collectors.toList());
  }

  public List<ForObjectTreeImpl> filterOnlyForObjectTreeImpl() {

    return objects.stream()
      .filter(child -> child instanceof ForObjectTreeImpl)
      .map(child -> (ForObjectTreeImpl) child )
      .collect(Collectors.toList());
  }

  public int getNumberElementsContainedInAnObject(ObjectTreeImpl objectTree) {
    return objectTree.properties().size();
  }


  public int getTotalNumberOfElementsOfNormalObjects(List<ObjectTreeImpl> objectTrees) {
    int count = 0;
    for (ObjectTreeImpl objectTree: objectTrees) {
      count += getNumberElementsContainedInAnObject(objectTree);
    }
    return count;
  }


  public int getTotalNumberOfElementsOfDifferentObjects() {
    int count = 0;
    List<ObjectTreeImpl> tuples = filterOnlyObjectTreeImpl();
    count += getTotalNumberOfElementsOfNormalObjects(tuples);
    List<ForObjectTreeImpl> forTuples = filterOnlyForObjectTreeImpl();
    count += forTuples.size();
    return count;
  }

  public double avgNumberOfElementsPerDifferentObjects() {
    if (!objects.isEmpty()) {
      double avgNumberOfElementsPerDifferentObjects = (double) this.getTotalNumberOfElementsOfDifferentObjects() / objects.size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfElementsPerDifferentObjects() {

    List<ObjectTreeImpl> localObjects = filterOnlyObjectTreeImpl();
    int max = 0;

    if (!localObjects.isEmpty()) {
      max = getNumberElementsContainedInAnObject(localObjects.get(0));
      for (ObjectTreeImpl objectTree: localObjects) {
        int tmpValue = getNumberElementsContainedInAnObject(objectTree);
        if (max < tmpValue) {
          max = tmpValue;
        }
      }
    }

    if (max == 0) {
      List<ForObjectTreeImpl> forTuples = filterOnlyForObjectTreeImpl();
      if (forTuples.size() > 0 ) {
        max = 1;
      }
    }
    return max;

  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    int numElementObjects = this.getTotalNumberOfElementsOfDifferentObjects();
    double avgElementObjects = this.avgNumberOfElementsPerDifferentObjects();
    int maxElementObjects = this.maxNumberOfElementsPerDifferentObjects();
    metrics.put("numElemObjects", numElementObjects);
    metrics.put("avgElemObjects", avgElementObjects);
    metrics.put("maxElemObjects", maxElementObjects);
    return metrics;
  }

}

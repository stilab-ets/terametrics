package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.ForObjectTreeImpl;
import org.sonar.iac.terraform.tree.impl.ObjectTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;

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
      return (double) this.getTotalNumberOfElementsOfDifferentObjects() / objects.size();
    }
    return 0.0;
  }

  public int maxNumberOfElementsPerDifferentObjects() {

    List<ObjectTreeImpl> objects = filterOnlyObjectTreeImpl();
    int max = 0;

    if (!objects.isEmpty()) {
      max = getNumberElementsContainedInAnObject(objects.get(0));
      for (ObjectTreeImpl objectTree: objects) {
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

    metrics.put("numElemTuples", numElementObjects);
    metrics.put("avgElemTuples", avgElementObjects);
    metrics.put("maxElemTuples", maxElementObjects);

    return metrics;
  }

}

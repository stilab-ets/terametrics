package org.stilab.metrics.counter.block_level;

import org.sonar.iac.terraform.tree.impl.ForTupleTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TupleElementsIdentifier {

  public List<TerraformTreeImpl> tupleTrees = new ArrayList<>();
  public TupleElementsIdentifier(List<TerraformTreeImpl> tupleTrees){
    this.tupleTrees = tupleTrees;
  }

  public List<TupleTreeImpl> filterOnlyTupleTreeImpl(List<TerraformTreeImpl> terraformTreeList) {

    return terraformTreeList.stream()
      .filter(child -> child instanceof TupleTreeImpl)
      .map(child -> (TupleTreeImpl) child )
      .collect(Collectors.toList());
  }

  public List<ForTupleTreeImpl> filterOnlyForTupleTreeImpl(List<TerraformTreeImpl> forTupleTreeImplList) {

    return forTupleTreeImplList.stream()
      .filter(child -> child instanceof ForTupleTreeImpl)
      .map(child -> (ForTupleTreeImpl) child )
      .collect(Collectors.toList());
  }

  public int getNumberOfElementsContainedInATuple(TupleTreeImpl tupleTree) {
    int count = 0;
    Iterator<?> iterator = tupleTree.iterator();
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    return count;
  }

  public int getTotalNumberOfElementsOfNormalTuples(List<TupleTreeImpl> tupleTrees) {
    int count = 0;
    for (TupleTreeImpl tupleTree: tupleTrees) {
      count += getNumberOfElementsContainedInATuple(tupleTree);
    }
    return count;
  }

  public int getTotalNumberOfElementsOfDifferentTuples() {
    int count = 0;
    List<TupleTreeImpl> tuples = filterOnlyTupleTreeImpl(tupleTrees);
    count += getTotalNumberOfElementsOfNormalTuples(tuples);
    List<ForTupleTreeImpl> forTuples = filterOnlyForTupleTreeImpl(tupleTrees);
    count += forTuples.size();
    return count;
  }

  public double avgNumberOfElementsPerDifferentTuples() {
    if (tupleTrees.size() >= 1) {
      return (double) getTotalNumberOfElementsOfDifferentTuples() / tupleTrees.size();
    }
    return 0.0;
  }

  public int maxNumberOfElementsPerDifferentTuples() {
    List<TupleTreeImpl> tuples = filterOnlyTupleTreeImpl(tupleTrees);
    int max = 0;
    for (TupleTreeImpl objectTree: tuples) {
      int tmpValue = getNumberOfElementsContainedInATuple(objectTree);
      if (max <= tmpValue) {
        max = tmpValue;
      }
    }
    if (max == 0) {
      List<ForTupleTreeImpl> forTuples = filterOnlyForTupleTreeImpl(tupleTrees);
      if (forTuples.size() > 0 ) {
        max = 1;
      }
    }
    return max;
  }

}

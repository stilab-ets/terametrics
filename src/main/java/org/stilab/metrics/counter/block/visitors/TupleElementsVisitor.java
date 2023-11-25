package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.ForTupleTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TupleElementsVisitor {

    private List<TerraformTreeImpl> tupleTrees;
    public TupleElementsVisitor(List<TerraformTreeImpl> tupleTrees){
      this.tupleTrees = tupleTrees;
    }

    public List<TupleTreeImpl> filterOnlyTupleTreeImpl(List<TerraformTreeImpl> terraformTreeList) {

      return terraformTreeList.stream()
        .filter(TupleTreeImpl.class::isInstance)
        .map(TupleTreeImpl.class::cast)
        .collect(Collectors.toList());
    }

    public List<ForTupleTreeImpl> filterOnlyForTupleTreeImpl(List<TerraformTreeImpl> forTupleTreeImplList) {

      return forTupleTreeImplList.stream()
        .filter(ForTupleTreeImpl.class::isInstance)
        .map(ForTupleTreeImpl.class::cast)
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
      if (!tupleTrees.isEmpty()) {
        double avgNumberOfElementsPerDifferentTuples = (double) getTotalNumberOfElementsOfDifferentTuples() / tupleTrees.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentTuples).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfElementsPerDifferentTuples() {

      int max = 0;
      List<TupleTreeImpl> tuples = filterOnlyTupleTreeImpl(tupleTrees);

      if (!tuples.isEmpty()) {
        max = getNumberOfElementsContainedInATuple(tuples.get(0));
        for (TupleTreeImpl objectTree: tuples) {
          int tmpValue = getNumberOfElementsContainedInATuple(objectTree);
          if (max < tmpValue) {
            max = tmpValue;
          }
        }
      }
      // max values
      if (max == 0) {
        List<ForTupleTreeImpl> forTuples = filterOnlyForTupleTreeImpl(tupleTrees);
        if (!forTuples.isEmpty()) {
          max = 1;
        }
      }
      return max;
    }

//    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
//
//      int numElemTuples = this.getTotalNumberOfElementsOfDifferentTuples();
//      double avgElemTuples = this.avgNumberOfElementsPerDifferentTuples();
//      int maxElemTuples = this.maxNumberOfElementsPerDifferentTuples();
//
//      metrics.put("numElemTuples", numElemTuples);
//      metrics.put("avgElemTuples", avgElemTuples);
//      metrics.put("maxElemTuples", maxElemTuples);
//
//      return metrics;
//    }
}

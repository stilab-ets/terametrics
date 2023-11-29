package org.stilab.calculators;

public class TupleElementsCalculator {
//  public int getNumberOfElementsContainedInATuple(TupleTreeImpl tupleTree) {
//    int count = 0;
//    Iterator<?> iterator = tupleTree.iterator();
//    while (iterator.hasNext()) {
//      iterator.next();
//      count++;
//    }
//    return count;
//  }
//
//  public int getTotalNumberOfElementsOfNormalTuples(List<TupleTreeImpl> tupleTrees) {
//    int count = 0;
//    for (TupleTreeImpl tupleTree: tupleTrees) {
//      count += getNumberOfElementsContainedInATuple(tupleTree);
//    }
//    return count;
//  }
//
//  public int getTotalNumberOfElementsOfDifferentTuples() {
//    int count = 0;
//    List<TupleTreeImpl> tuples = filterOnlyTupleTreeImpl(tupleTrees);
//    count += getTotalNumberOfElementsOfNormalTuples(tuples);
//    List<ForTupleTreeImpl> forTuples = filterOnlyForTupleTreeImpl(tupleTrees);
//    count += forTuples.size();
//    return count;
//  }
//
//  public double avgNumberOfElementsPerDifferentTuples() {
//    if (!tupleTrees.isEmpty()) {
//      double avgNumberOfElementsPerDifferentTuples = (double) getTotalNumberOfElementsOfDifferentTuples() / tupleTrees.size();
//      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentTuples).setScale(2, RoundingMode.HALF_UP);
//      return roundedAverage.doubleValue();
//    }
//    return 0.0;
//  }
//
//  public int maxNumberOfElementsPerDifferentTuples() {
//
//    int max = 0;
//    List<TupleTreeImpl> tuples = filterOnlyTupleTreeImpl(tupleTrees);
//
//    if (!tuples.isEmpty()) {
//      max = getNumberOfElementsContainedInATuple(tuples.get(0));
//      for (TupleTreeImpl objectTree: tuples) {
//        int tmpValue = getNumberOfElementsContainedInATuple(objectTree);
//        if (max < tmpValue) {
//          max = tmpValue;
//        }
//      }
//    }
//    // max values
//    if (max == 0) {
//      List<ForTupleTreeImpl> forTuples = filterOnlyForTupleTreeImpl(tupleTrees);
//      if (!forTuples.isEmpty()) {
//        max = 1;
//      }
//    }
//    return max;
//  }
}

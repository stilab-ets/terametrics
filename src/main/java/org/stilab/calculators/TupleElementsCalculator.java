package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.ForTupleTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;
import org.stilab.visitors.TupleElementsVisitor;
import org.stilab.visitors.TupleVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

public class TupleElementsCalculator {

    private TupleElementsVisitor tupleElementsVisitor;

    public TupleElementsCalculator(BlockTreeImpl identifiedBlock) {
      TupleVisitor tupleVisitor = new TupleVisitor();
      List<TerraformTreeImpl> tuples = tupleVisitor.filterTuplesFromBlock(identifiedBlock);
      tupleElementsVisitor = new TupleElementsVisitor(tuples);
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
      List<TupleTreeImpl> tuples = tupleElementsVisitor.filterOnlyTupleTreeImpl(tupleElementsVisitor.getTupleTrees());
      count += getTotalNumberOfElementsOfNormalTuples(tuples);
      List<ForTupleTreeImpl> forTuples = tupleElementsVisitor.filterOnlyForTupleTreeImpl(tupleElementsVisitor.getTupleTrees());
      count += forTuples.size();
      return count;
    }

    public double avgNumberOfElementsPerDifferentTuples() {
      if (!tupleElementsVisitor.getTupleTrees().isEmpty()) {
        double avgNumberOfElementsPerDifferentTuples = (double) getTotalNumberOfElementsOfDifferentTuples() / tupleElementsVisitor.getTupleTrees().size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentTuples).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfElementsPerDifferentTuples() {

      int max = 0;
      List<TupleTreeImpl> tuples = tupleElementsVisitor.filterOnlyTupleTreeImpl(tupleElementsVisitor.getTupleTrees());

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
        List<ForTupleTreeImpl> forTuples = tupleElementsVisitor.filterOnlyForTupleTreeImpl(tupleElementsVisitor.getTupleTrees());
        if (!forTuples.isEmpty()) {
          max = 1;
        }
      }
      return max;
    }
}

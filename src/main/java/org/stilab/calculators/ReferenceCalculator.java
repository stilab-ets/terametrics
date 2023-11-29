package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ReferenceVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReferenceCalculator {

    private ReferenceVisitor referenceVisitor;

    public ReferenceCalculator(BlockTreeImpl identifiedBlock) {
      referenceVisitor = new ReferenceVisitor();
      referenceVisitor.filterAttributeAccessFromBlock(identifiedBlock);
    }

    public int totalAttributeAccess(){
      return referenceVisitor.getPointers().size();
    }

    public double avgAttributeAccess(){
      if (!referenceVisitor.getAttributes().isEmpty()) {
        double avgNumberOfElementsPerDifferentObjects = (double) referenceVisitor.getPointers().size() / referenceVisitor.getAttributes().size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxAttributeAccess(){

      if (referenceVisitor.getAttributes().isEmpty()) { return 0; }

      int max = referenceVisitor.visit(referenceVisitor.getAttributes().get(0)).size();
      for (AttributeTreeImpl attribute: referenceVisitor.getAttributes()) {
        int value = referenceVisitor.visit(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
    }

}

package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.MccabeCC;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MccabeCCCalculator {


  private MccabeCC mccabeCC;

  public MccabeCCCalculator(BlockTreeImpl identifiedBlock){
    mccabeCC = new MccabeCC();
    mccabeCC.setAttributes(identifiedBlock);
  }

  public int sumMccabeCC() {
    int sum = 0;
    for (AttributeTreeImpl attribute: mccabeCC.getAttributes()) {
      sum += mccabeCC.measureMccabeCCForAnAttributes(attribute);
    }
    return sum;
  }

  public double avgMccabeCC() {
    if (!mccabeCC.getAttributes().isEmpty()) {
      double avgMccabeCC = (double) sumMccabeCC() / mccabeCC.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgMccabeCC).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxMccabeCC(){

    if (mccabeCC.getAttributes().isEmpty()){ return 0;}

    int max = mccabeCC.measureMccabeCCForAnAttributes(mccabeCC.getAttributes().get(0));

    for (AttributeTreeImpl attribute: mccabeCC.getAttributes()) {
      int value = mccabeCC.measureMccabeCCForAnAttributes(attribute);
      if (max < value) {
        max = value;
      }
    }

    return max;
  }

}

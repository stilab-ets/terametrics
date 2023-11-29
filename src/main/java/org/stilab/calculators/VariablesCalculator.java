package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.VariablesVisitor;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class VariablesCalculator {

  private VariablesVisitor variablesVisitor;

  public VariablesCalculator(BlockTreeImpl identifiedBlock) {
    this.variablesVisitor = new VariablesVisitor();
    variablesVisitor.filterVarsFromBlock(identifiedBlock);
  }

  public int totalNumberOfVars(){
    return this.variablesVisitor.getVariables().size();
  }

  public double avgNumberOfVars(){
    if (!variablesVisitor.getAttributes().isEmpty()) {
      double avgNumberOfElementsPerDifferentObjects = (double) totalNumberOfVars() / variablesVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfElementsPerDifferentObjects).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfVars(){
    if (variablesVisitor.getAttributes().isEmpty()){ return 0; }

    int max = variablesVisitor.visit(variablesVisitor.getAttributes().get(0)).size();
    for (AttributeTreeImpl attribute: variablesVisitor.getAttributes()){
      int value = variablesVisitor.visit(attribute).size();
      if ( value > max ){
        max = value;
      }
    }
    return max;
  }
}

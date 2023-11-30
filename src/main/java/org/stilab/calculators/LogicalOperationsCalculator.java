package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.LogicalOperationsVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LogicalOperationsCalculator  {

  private LogicalOperationsVisitor logicalOperationsVisitor;

  public LogicalOperationsCalculator(BlockTreeImpl identifiedBlock) {
    logicalOperationsVisitor = new LogicalOperationsVisitor();
    logicalOperationsVisitor.filterLogicalOperationsFromBlock(identifiedBlock);
  }


  public int totalNumberOfLogicalOperations(){
    return logicalOperationsVisitor.getDecisions().size();
  }

  public double avgNumberOfLogicalOperations(){
    if(!logicalOperationsVisitor.getAttributes().isEmpty()){
      double avgNumberOfLogicalOperations = (double) totalNumberOfLogicalOperations() / logicalOperationsVisitor.getAttributes().size();
      BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfLogicalOperations).setScale(2, RoundingMode.HALF_UP);
      return roundedAverage.doubleValue();
    }
    return 0.0;
  }

  public int maxNumberOfLogicalOperations(){

    if (logicalOperationsVisitor.getAttributes().isEmpty()){ return 0; }
    int max = logicalOperationsVisitor.visit(logicalOperationsVisitor.getAttributes().get(0)).size();

    for (AttributeTreeImpl attribute: logicalOperationsVisitor.getAttributes()) {
      int value = logicalOperationsVisitor.visit(attribute).size();
      if (value > max) {
        max = value;
      }
    }
    return max;

  }


}

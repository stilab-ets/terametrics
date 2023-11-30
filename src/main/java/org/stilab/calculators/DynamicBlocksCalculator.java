package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.DynamicBlocksVisitor;

public class DynamicBlocksCalculator {

    private DynamicBlocksVisitor dynamicBlocksVisitor;

    public DynamicBlocksCalculator(BlockTreeImpl identifiedBlock){
      dynamicBlocksVisitor = new DynamicBlocksVisitor();
      dynamicBlocksVisitor.filterDynamicBlock(identifiedBlock);
    }

    public int countDynamicBlocks(){
      return dynamicBlocksVisitor.countDynamicBlock();
    }
}

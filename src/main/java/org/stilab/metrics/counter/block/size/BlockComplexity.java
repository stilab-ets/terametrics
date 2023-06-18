package org.stilab.metrics.counter.block.size;

import org.stilab.interfaces.IBlockComplexity;
import org.stilab.utils.ServiceCounter;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public class BlockComplexity implements IBlockComplexity {


  private String blockContent;
  private BlockTreeImpl blockTree;
  private int startLine = 0;
  private int endLine = 0;
  private int totalLines = 0;


  public BlockComplexity(BlockTreeImpl blockTree, String blockContent) {
    this.blockTree    = blockTree;
    this.startLine    = blockTree.value().textRange().start().line();
    this.endLine      = blockTree.value().textRange().end().line();
    this.blockContent = blockContent;
  }

  public BlockComplexity(String filePath, BlockTreeImpl blockTree) {

    this.blockTree    = blockTree;
    this.startLine    = blockTree.value().textRange().start().line();
    this.endLine      = blockTree.value().textRange().end().line();
    this.blockContent = ServiceCounter.getInstance().parseFileContentByPart(filePath, this.startLine,
                                                                              this.endLine);
  }

  @Override
  public int depthOfBlock() {
    return this.totalLines =  this.endLine - this.startLine + 1;
  }

  @Override
  public int LOC() {

//    int blanks   = ServiceCounter.getInstance().countBlankLinesInsideBlock(this.blockContent);
//    int comments = ServiceCounter.getInstance().countCommentsLines(this.blockContent);
//
//    int value =  this.totalLines - blanks - comments ;

      int value = ServiceCounter.getInstance().countLineOfCode(this.blockContent);
//    return value;
    return Math.max(value, 0);
  }

  @Override
  public int NLOC() {
//    int value = this.totalLines - LOC();

    int blanks   = ServiceCounter.getInstance().countBlankLinesInsideBlock(this.blockContent);
    int comments = ServiceCounter.getInstance().countCommentsLines(this.blockContent);

    return Math.max(blanks + comments, 0);

  }

}

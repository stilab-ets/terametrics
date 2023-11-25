package org.stilab.metrics.counter.block.visitors;

import org.stilab.utils.ServiceCounter;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public class BlockComplexity{
      private String blockContent;
      private int startLine = 0;
      private int endLine = 0;

      public BlockComplexity(BlockTreeImpl blockTree, String blockContent) {
        this.startLine    = blockTree.value().textRange().start().line();
        this.endLine      = blockTree.value().textRange().end().line();
        this.blockContent = blockContent;
      }

      public String getBlockContent() {
        return this.blockContent;
      }

      public BlockComplexity(String filePath, BlockTreeImpl blockTree) {
        this.startLine    = blockTree.value().textRange().start().line();
        this.endLine      = blockTree.value().textRange().end().line();
        this.blockContent = ServiceCounter.getInstance().parseFileContentByPart(filePath, this.startLine, this.endLine);
      }

      public int depthOfBlock() {
        return this.endLine - this.startLine + 1;
      }

      public int numberCodeLines() {
          int value = ServiceCounter.getInstance().countLineOfCode(this.blockContent);
          return Math.max(value, 0);
      }

      public int numberNonCodeLines() {
        int blanks   = ServiceCounter.getInstance().countBlankLinesInsideBlock(this.blockContent);
        int comments = ServiceCounter.getInstance().countCommentsLines(this.blockContent);
        return Math.max(blanks + comments, 0);
      }

}

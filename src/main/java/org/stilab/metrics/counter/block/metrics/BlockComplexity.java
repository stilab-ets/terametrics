package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.stilab.interfaces.IBlockComplexity;
import org.stilab.utils.ServiceCounter;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public class BlockComplexity implements IBlockComplexity {
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

      @Override
      public int depthOfBlock() {
        return this.endLine - this.startLine + 1;
      }

      @Override
      public int number_code_lines() {
          int value = ServiceCounter.getInstance().countLineOfCode(this.blockContent);
          return Math.max(value, 0);
      }

      @Override
      public int number_non_code_lines() {
        int blanks   = ServiceCounter.getInstance().countBlankLinesInsideBlock(this.blockContent);
        int comments = ServiceCounter.getInstance().countCommentsLines(this.blockContent);
        return Math.max(blanks + comments, 0);
      }

      public int countBlank() {
        return ServiceCounter.getInstance().countBlankLinesInsideBlock(this.blockContent);
      }

      public int countComment() {
        return ServiceCounter.getInstance().countCommentsLines(this.blockContent);
      }
      public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

        int depthOfBlock = this.depthOfBlock();
        metrics.put("depthOfBlock", depthOfBlock);
        //  11. Number of lines of code
        int loc = this.number_code_lines();
        metrics.put("loc", loc);
        //  12. Number of Non-line of code ( comments + blank)
        int nloc = this.number_non_code_lines();
        metrics.put("nloc", nloc);

        return metrics;
      }
}

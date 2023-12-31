package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.BlockComplexity;

public class BlockComplexityCollector implements Decorator {
      private String blockAsString;
      public BlockComplexityCollector(String blockAsString) {
        this.blockAsString = blockAsString;
      }
      @Override
      public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
        BlockComplexity blockComplexity = new BlockComplexity(identifiedBlock, blockAsString);
        int depthOfBlock = blockComplexity.depthOfBlock();
        metrics.put("depthOfBlock", depthOfBlock);
        //  11. Number of lines of code
        int loc = blockComplexity.numberCodeLines();
        metrics.put("loc", loc);
        //  12. Number of Non-line of code ( comments + blank)
        int nloc = blockComplexity.numberNonCodeLines();
        metrics.put("nloc", nloc);
        return metrics;
      }
}

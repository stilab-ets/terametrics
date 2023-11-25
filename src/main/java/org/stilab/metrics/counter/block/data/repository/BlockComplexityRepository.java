package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.visitors.BlockComplexity;

public class BlockComplexityRepository implements Repository {
      private String filePath;
      public BlockComplexityRepository(String filePath) {
        this.filePath = filePath;
      }
      @Override
      public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
        BlockComplexity blockComplexity = new BlockComplexity(filePath, identifiedBlock);
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

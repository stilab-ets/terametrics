package org.stilab.block;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;

import java.io.File;
import java.util.List;

public class NestedBlocksTest  extends TestCase {

    private BlockTreeImpl identifiedBlock;
    private JSONObject metrics;

    @Override
    protected void setUp() throws Exception {

      super.setUp();

      File base = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions\\terraform_miner\\src\\test\\java\\org\\stilab\\block\\nested_block.tf");
      HclParser hclParser = new HclParser();
      Tree tree = hclParser.parse(base);

      // To look for the Parsed One
      TopBlockFinder topBlockFinder = new TopBlockFinder();
      List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
      identifiedBlock = blocks.get(0);
      metrics = new JSONObject();
    }

    public void testNestedBlockAverageLength() {
      NestedBlockIdentifier nestedBlockIdentifier = new NestedBlockIdentifier();
      nestedBlockIdentifier.getAllNestedBlocks(identifiedBlock);
























    }

}

package org.stilab.utils;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.finder.BlockFinderByIndex;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;

import java.util.List;

public class BlockServiceLocator {

   public BlockServiceLocator() {}

   public Pair<BlockTreeImpl, String> identifyRightBlock(String fileContent, int impactedLine) {

       // Hcl Parser
       HclParser hclParser = new HclParser();

       // Parse the content of the hcl file
       Tree tree = hclParser.parse(fileContent);

       // To look for the Parsed One
       TopBlockFinder topBlockFinder = new TopBlockFinder();

       List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

       // Search By Index
       BlockFinderByIndex blockFinderByIndex = new BlockFinderByIndex();

       // Search identify the right block
       BlockTreeImpl identifiedBlock = blockFinderByIndex.findBlockByIndex(blocks, impactedLine);

      // When the change doesn't concern a block of tf
       if (identifiedBlock != null) {

         String blockContent = ServiceCounter.getInstance().extractDesiredContent(fileContent,
           identifiedBlock.key().textRange().start().line(),
           identifiedBlock.value().textRange().end().line()
         );
         return new Pair<>(identifiedBlock, blockContent);
       }

       return new Pair<>(null, "");
   }


}

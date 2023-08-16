package org.stilab.metrics.counter.block.finder;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.List;
import java.util.stream.Collectors;

public class BlockFinderByIndex {
    public BlockTreeImpl findBlockByIndex(List<BlockTreeImpl> blocks, int indexOfImpactedLine) {
      for (BlockTreeImpl blockTree: blocks) {
        int startOfBlock = blockTree.value().textRange().start().line();
        int endOfBlock = blockTree.value().textRange().end().line();
        if ( (indexOfImpactedLine >= startOfBlock) && (indexOfImpactedLine <= endOfBlock)) {
          return blockTree;
        }
      }
      return null;
    }
}

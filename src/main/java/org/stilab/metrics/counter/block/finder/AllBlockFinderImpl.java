package org.stilab.metrics.counter.block.finder;

import org.stilab.interfaces.AllBlockFinder;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllBlockFinderImpl implements AllBlockFinder {

  @Override
  public List<BlockTreeImpl> getAllBlock(Tree root) {

    NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();

     List<BlockTreeImpl> blocks = new ArrayList<>();

     List<BlockTreeImpl> blockTrees = root.children().stream().filter(node -> node instanceof BlockTreeImpl).map(node -> (BlockTreeImpl) node)
      .collect(Collectors.toList());

     blocks.addAll(blockTrees);

     for (BlockTreeImpl blockTree: blockTrees) {

       Tree bodyTree = (new BodyTreeFinder()).find(blockTree);

       blocks.addAll( nestedBlockCounter.getAllNestedBlocks(bodyTree) );

     }

    return blocks;
  }

}

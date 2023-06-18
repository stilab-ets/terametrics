package org.stilab.metrics.counter.attr.counter;

import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import org.stilab.metrics.counter.block.finder.BodyTreeFinder;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.interfaces.AttributeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.List;

public class AttrFileCounterImpl implements AttributeCounter {
  @Override
  public int countAttribute(Tree tree) {

    BodyTreeFinder bodyTreeFinder = new BodyTreeFinder();
    AttributeCounter attributeCounter = new AttributeCounterImpl();
    NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

    int topAttributesInEachBlock = blocks.stream()
      .map(blockTree -> {
        Tree tree1 = bodyTreeFinder.find(blockTree);
        List<BlockTreeImpl> nestedBlocks = nestedBlockCounter.getAllNestedBlocks(tree1);

        int attributesInBlock = attributeCounter.countAttribute(tree1);

        int attributesInNestedBlocks = nestedBlocks.stream()
          .map(blockTree1 -> {
            Tree tree2 = bodyTreeFinder.find(blockTree1);
            int count = attributeCounter.countAttribute(tree2);
            System.out.println(count);
            return count;
          })
          .reduce(0, Integer::sum);

        return attributesInBlock + attributesInNestedBlocks;
      })
      .reduce(0, Integer::sum);

    return topAttributesInEachBlock;

  }
}

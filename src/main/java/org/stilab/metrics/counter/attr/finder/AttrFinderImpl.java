package org.stilab.metrics.counter.attr.finder;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttrFinderImpl {
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public AttrFinderImpl() {}

    // You should add also for the top attributes
    public List<AttributeTreeImpl> getAttributes(BlockTreeImpl nested) {

      List<AttributeTreeImpl> topAttributes = nested.value().statements()
        .stream()
        .filter(child -> child instanceof AttributeTreeImpl)
        .map(child -> (AttributeTreeImpl) child)
        .collect(Collectors.toList());

      return topAttributes;
    }

    // You should add also for the all attributes (tops + blocks)
    public List<AttributeTreeImpl> getAllAttributes(BlockTreeImpl rootBlock) {
      NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
      List<BlockTreeImpl> blockTreeList = new ArrayList<>();
      blockTreeList.addAll(nestedBlockCounter.getAllNestedBlocks(rootBlock));
      // loop over the blocks
      for (BlockTreeImpl blockTree: blockTreeList) {
        attributes.addAll( getAttributes(blockTree) );
      }
      return attributes;
    }
    public int countAttributesPerTopBlock() {
      return attributes.size();
    }
}

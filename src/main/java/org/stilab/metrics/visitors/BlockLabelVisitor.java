package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlockLabelVisitor {
  public BlockLabelVisitor() {
//    Constructor
  }
  public List<String> identifyLabelsOfBlock(BlockTreeImpl blockTree) {
    // Assuming blockTree.labels() returns an object with a method to retrieve labels
   return IntStream.range(0, blockTree.labels().size())
      .mapToObj(i -> blockTree.labels().get(i).value())
      .collect(Collectors.toList());
  }
}

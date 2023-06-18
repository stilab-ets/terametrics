package org.stilab.metrics.counter.expression;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;

import java.util.ArrayList;
import java.util.List;

public class MccabeCC {

  public int measureMccabeCCForAnAttributes(
    AttributeTreeImpl attributeTree
  ) {

    List<TerraformTreeImpl> binaryConditions =  (new ConditionalExpressionIdentifier())
                                                    .filterConditions(attributeTree);
    return binaryConditions.size() + 1;
  }

  public double avgMccabeCC(BlockTreeImpl blockTree) {

    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    List<Integer> localMcc = new ArrayList<>();
    int right = 0;
//
    for (AttributeTreeImpl attribute: attributeTrees) {
//      System.out.println( measureMccabeCCForAnAttributes(attribute) == 0);
      if (measureMccabeCCForAnAttributes(attribute) != 0) {
        right = right + 1;
        localMcc.add( measureMccabeCCForAnAttributes(attribute) );
      }
    }

    if (right == 0) {
      return 0;
    }

    return (double) localMcc.stream().mapToInt(Integer::intValue).sum() / (double) right;
  }
}

package org.stilab.metrics.counter.block_level;

import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SplatExpressionIdentifier {

  public List<TerraformTreeImpl> splatExpressions = new ArrayList<>();
  public List<AttributeTreeImpl> attributes = new ArrayList<>();

  public List<TerraformTreeImpl> filterSplats(AttributeTreeImpl attributeTree ) {
    ExpressionTree expressionTree = attributeTree.value();
    List<Tree> trees = ExpressionAnalyzer.getInstance().
      getAllNestedExpressions(expressionTree);

    Stream<TerraformTreeImpl> splatAccessFilter = trees.stream()
      .filter(child -> child instanceof AttributeSplatAccessTreeImpl)
      .map(child -> (TerraformTreeImpl) child);

    Stream<TerraformTreeImpl> indexSplatAccess = trees.stream()
      .filter(child -> child instanceof IndexSplatAccessTreeImpl)
      .map( child -> (TerraformTreeImpl) child);

    Stream<TerraformTreeImpl> combinedFilters = Stream.concat(
      splatAccessFilter, indexSplatAccess
    );
    return combinedFilters.collect(Collectors.toList());
  }

  public List<TerraformTreeImpl> filterSplatFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
    List<TerraformTreeImpl> splats = new ArrayList<>();
    for (AttributeTreeImpl attribute: attributeTrees) {
      splats.addAll(this.filterSplats(attribute)  );
    }
    return splats;
  }

  public List<TerraformTreeImpl> filtersSplatsFromBlock(BlockTreeImpl blockTree) {
    attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
    splatExpressions = this.filterSplatFromAttributesList(attributes);
    return splatExpressions;
  }


  public int totalSplatExpressions() {
    return splatExpressions.size();
  }

  public double avgSplatExpressions() {
    if (attributes.size()>0) {
      return (double) totalSplatExpressions() / attributes.size();
    }
    return 0.0;
  }

  public int maxSplatExpressions() {
    int max = 0;
    for(AttributeTreeImpl attribute: attributes) {
      int value = filterSplats(attribute).size();
      if (value >= max) {
        max = value;
      }
    }
    return max;
  }
}

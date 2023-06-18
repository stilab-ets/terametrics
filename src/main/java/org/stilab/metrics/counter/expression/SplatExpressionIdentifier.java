package org.stilab.metrics.counter.expression;

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


  public List<TerraformTreeImpl> splatExpressions
    = new ArrayList<>();

  public List<TerraformTreeImpl> filterSplats(
    AttributeTreeImpl attributeTree
  ) {
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

  public List<TerraformTreeImpl> filterSplatFromAttributesList(
    List<AttributeTreeImpl> attributeTrees
  ) {
    List<TerraformTreeImpl> splats = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributeTrees) {
      splats.addAll(this.filterSplats(attribute)  );
    }
    return splats;
  }

  public List<TerraformTreeImpl> filtersConditionsFromBlock(
    BlockTreeImpl blockTree
  ) {

    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);

    this.splatExpressions =
      this.filterSplatFromAttributesList(attributeTrees);

    return this.splatExpressions;
  }

  public int countSplats() {
    return this.splatExpressions.size();
  }

}

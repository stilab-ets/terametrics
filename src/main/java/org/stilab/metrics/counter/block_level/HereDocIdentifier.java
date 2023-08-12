package org.stilab.metrics.counter.block_level;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;

import java.util.ArrayList;
import java.util.List;

public class HereDocIdentifier {
  public List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();
  LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
  LiteralExpressionHereDocIdentifier literalExpressionHereDocIdentifier = new LiteralExpressionHereDocIdentifier();
  public HereDocIdentifier() {}
  public List<LiteralExprTreeImpl> filterHereDoc(AttributeTreeImpl attributeTree){
    List<LiteralExprTreeImpl> exprs = literalExpressionIdentifier.filterLiteralExpr(attributeTree);
    return literalExpressionHereDocIdentifier.filterHereDocFromLiteralExpressions(exprs);
  }
  public List<LiteralExprTreeImpl> filterHereDocsFromAttributesList(List<AttributeTreeImpl> attributeTrees){
    List<LiteralExprTreeImpl> attributeAccessTrees = new ArrayList<>();
    for(AttributeTreeImpl attributeAccess: attributeTrees) {
      attributeAccessTrees.addAll( this.filterHereDoc(attributeAccess) );
    }
    return attributeAccessTrees;
  }
  // List<LiteralExprTreeImpl>
  public void filterHereDocsFromBlock(BlockTreeImpl blockTree) {
    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAllAttributes(blockTree);
    this.hereDocs = this.filterHereDocsFromAttributesList(attributeTrees);
  // return this.hereDocs;
  }

  public int totalNumberOfHereDoc(){
    return this.hereDocs.size();
  }

  public double avgNumberLinesPerHereDoc(){
    return this.literalExpressionHereDocIdentifier
      .avgNumberLinesPerHereDoc(this.hereDocs);
  }

  public int maxNumberLinesPerHereDoc() {
    return this.literalExpressionHereDocIdentifier
      .maxNumberLinesPerHereDoc(this.hereDocs);
  }
}

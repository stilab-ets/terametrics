package org.stilab.visitors;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HereDocVisitor {
    private List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();

  private List<AttributeTreeImpl> attributes = new ArrayList<>();
    LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();
    LiteralExpressionHereDocVisitor literalExpressionHereDocVisitor = new LiteralExpressionHereDocVisitor();

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }

    public List<LiteralExprTreeImpl> getHereDocs() {
      return hereDocs;
    }

    public LiteralExpressionHereDocVisitor getLiteralExpressionHereDocVisitor() {
      return literalExpressionHereDocVisitor;
    }

    public LiteralExpressionVisitor getLiteralExpressionVisitor() {
      return literalExpressionVisitor;
    }

  public List<LiteralExprTreeImpl> visit(AttributeTreeImpl attributeTree){
      List<LiteralExprTreeImpl> exprs = literalExpressionVisitor.visit(attributeTree);
      return literalExpressionHereDocVisitor.filterHereDocFromLiteralExpressions(exprs);
    }
    public List<LiteralExprTreeImpl> filterHereDocsFromAttributesList(List<AttributeTreeImpl> attributeTrees){
      List<LiteralExprTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.visit(attributeAccess) );
      }
      return attributeAccessTrees;
    }

    public List<LiteralExprTreeImpl> filterHereDocsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      hereDocs = this.filterHereDocsFromAttributesList(attributes);
      return hereDocs;
    }



}

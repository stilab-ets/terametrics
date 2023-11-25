package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.block.data.repository.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HereDocVisitor {
    private List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();

  private List<AttributeTreeImpl> attributes = new ArrayList<>();
    LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();
    LiteralExpressionHereDocVisitor literalExpressionHereDocVisitor = new LiteralExpressionHereDocVisitor();
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

    public int totalNumberOfHereDoc(){
      return this.hereDocs.size();
    }

    public double avgNumberOfHereDoc() {
      if (!attributes.isEmpty()) {
        double avgNumberOfHereDoc = (double) totalNumberOfHereDoc() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfHereDoc).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int totalLinesOfHereDoc(){
      return this.literalExpressionHereDocVisitor.totalLinesOfHereDoc(this.hereDocs);
    }

    public double avgNumberLinesPerHereDoc(){
      return this.literalExpressionHereDocVisitor
        .avgNumberLinesPerHereDoc(this.hereDocs);
    }

    public int maxNumberLinesPerHereDoc() {
      return this.literalExpressionHereDocVisitor
        .maxNumberLinesPerHereDoc(this.hereDocs);
    }

}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HereDocIdentifier {
    private List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();

  private List<AttributeTreeImpl> attributes = new ArrayList<>();
    LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();
    LiteralExpressionHereDocIdentifier literalExpressionHereDocIdentifier = new LiteralExpressionHereDocIdentifier();
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
      return this.literalExpressionHereDocIdentifier.totalLinesOfHereDoc(this.hereDocs);
    }

    public double avgNumberLinesPerHereDoc(){
      return this.literalExpressionHereDocIdentifier
        .avgNumberLinesPerHereDoc(this.hereDocs);
    }

    public int maxNumberLinesPerHereDoc() {
      return this.literalExpressionHereDocIdentifier
        .maxNumberLinesPerHereDoc(this.hereDocs);
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      // Relative to Attributes
      this.filterHereDocsFromBlock(identifiedBlock);
      int numHereDocs = this.totalNumberOfHereDoc();
      double avgHereDocs = this.avgNumberOfHereDoc();
      metrics.put("numHereDocs", numHereDocs);
      metrics.put("avgHereDocs", avgHereDocs);

      // Relative to the size of here Doc
      double avgLinesHereDocs = this.avgNumberLinesPerHereDoc();
      int maxLinesHereDocs = this.maxNumberLinesPerHereDoc();
      int numLinesHereDocs = this.totalLinesOfHereDoc();

      metrics.put("avgLinesHereDocs", avgLinesHereDocs);
      metrics.put("maxLinesHereDocs", maxLinesHereDocs);
      metrics.put("numLinesHereDocs", numLinesHereDocs);
      return metrics;

    }

}

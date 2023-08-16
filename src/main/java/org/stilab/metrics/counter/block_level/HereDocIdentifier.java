package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import java.util.ArrayList;
import java.util.List;

public class HereDocIdentifier {
    public List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();

    public List<AttributeTreeImpl> attributes = new ArrayList<>();
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
        return (double) totalNumberOfHereDoc() / attributes.size();
      }
      return 0.0;
    }
    public int maxNumberOfHereDoc() {
      if (attributes.isEmpty()){ return 0; }

      int max = filterHereDoc(attributes.get(0)).size();

      for (AttributeTreeImpl attribute: attributes) {
        int value = filterHereDoc(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;
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
      int maxHereDocs = this.maxNumberOfHereDoc();
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

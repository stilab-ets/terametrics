package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.HereDocVisitor;

public class HereDocCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      HereDocVisitor hereDocVisitor = new HereDocVisitor();

      // Relative to Attributes
      hereDocVisitor.filterHereDocsFromBlock(identifiedBlock);
      int numHereDocs = hereDocVisitor.totalNumberOfHereDoc();
      double avgHereDocs = hereDocVisitor.avgNumberOfHereDoc();
      metrics.put("numHereDocs", numHereDocs);
      metrics.put("avgHereDocs", avgHereDocs);

      // Relative to the size of here Doc
      double avgLinesHereDocs = hereDocVisitor.avgNumberLinesPerHereDoc();
      int maxLinesHereDocs = hereDocVisitor.maxNumberLinesPerHereDoc();
      int numLinesHereDocs = hereDocVisitor.totalLinesOfHereDoc();

      metrics.put("avgLinesHereDocs", avgLinesHereDocs);
      metrics.put("maxLinesHereDocs", maxLinesHereDocs);
      metrics.put("numLinesHereDocs", numLinesHereDocs);
      return metrics;

    }

}

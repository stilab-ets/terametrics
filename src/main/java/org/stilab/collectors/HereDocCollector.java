package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.HereDocCalculator;
import org.stilab.visitors.HereDocVisitor;

public class HereDocCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      HereDocCalculator hereDocCalculator = new HereDocCalculator(identifiedBlock);

      int numHereDocs = hereDocCalculator.totalNumberOfHereDoc();
      double avgHereDocs = hereDocCalculator.avgNumberOfHereDoc();
      metrics.put("numHereDocs", numHereDocs);
      metrics.put("avgHereDocs", avgHereDocs);

      // Relative to the size of here Doc
      double avgLinesHereDocs = hereDocCalculator.avgNumberLinesPerHereDoc();
      int maxLinesHereDocs = hereDocCalculator.maxNumberLinesPerHereDoc();
      int numLinesHereDocs = hereDocCalculator.totalLinesOfHereDoc();

      metrics.put("avgLinesHereDocs", avgLinesHereDocs);
      metrics.put("maxLinesHereDocs", maxLinesHereDocs);
      metrics.put("numLinesHereDocs", numLinesHereDocs);
      return metrics;

    }

}

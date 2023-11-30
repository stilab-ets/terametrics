package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.HereDocVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HereDocCalculator {

     private HereDocVisitor hereDocVisitor;

     public HereDocCalculator(BlockTreeImpl identifiedBlock){
       hereDocVisitor = new HereDocVisitor();
       hereDocVisitor.filterHereDocsFromBlock(identifiedBlock);
     }

      public int totalNumberOfHereDoc(){
        return hereDocVisitor.getHereDocs().size();
      }

      public double avgNumberOfHereDoc() {
        if (!hereDocVisitor.getAttributes().isEmpty()) {
          double avgNumberOfHereDoc = (double) totalNumberOfHereDoc() / hereDocVisitor.getAttributes().size();
          BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfHereDoc).setScale(2, RoundingMode.HALF_UP);
          return roundedAverage.doubleValue();
        }
        return 0.0;
      }

      public int totalLinesOfHereDoc(){
        return hereDocVisitor.getLiteralExpressionHereDocVisitor().totalLinesOfHereDoc(hereDocVisitor.getHereDocs());
      }

      public double avgNumberLinesPerHereDoc(){
        return hereDocVisitor.getLiteralExpressionHereDocVisitor()
          .avgNumberLinesPerHereDoc(hereDocVisitor.getHereDocs());
      }

      public int maxNumberLinesPerHereDoc() {
        return hereDocVisitor.getLiteralExpressionHereDocVisitor()
          .maxNumberLinesPerHereDoc(hereDocVisitor.getHereDocs());
      }

}

package org.stilab.metrics.counter.block.metrics;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LiteralExpressionHereDocIdentifier {

    public LiteralExpressionHereDocIdentifier() {}
    private Matcher getHereDocMatcher(LiteralExprTreeImpl exprTree) {
      return Pattern.compile("<<(-)?([A-Z_]+)").matcher(exprTree.value());
    }

    public boolean isHereDocHere(LiteralExprTreeImpl exprTree){
      Matcher matcher = getHereDocMatcher(exprTree);
      return matcher.find();
    }

    public int numLinesPerHereDoc(LiteralExprTreeImpl exprTree) {
        String content = exprTree.value();
        Matcher matcher = getHereDocMatcher(exprTree);
        int numLines = 0;

        if (matcher.find()) {
          String delimiter = matcher.group(2);
          int startPos = matcher.end();
          int endPos = content.indexOf(delimiter, startPos);
          if (endPos != -1) {
            // Extract the heredoc content
            String heredocContent = content.substring(startPos, endPos);
            // Remove trailing newline characters and leading whitespace
            heredocContent = heredocContent.replaceAll("\\s+$", "");
            String[] lines = heredocContent.split("\n");
            numLines = lines.length-1;
            return numLines;
          }
        }
      return numLines;
    }

    public int totalLinesOfHereDoc(List<LiteralExprTreeImpl> exprTrees){
      List<LiteralExprTreeImpl> hereDocs = filterHereDocFromLiteralExpressions(exprTrees);
      int sum = 0;
      for (LiteralExprTreeImpl exprTree: hereDocs) {
          sum += numLinesPerHereDoc(exprTree);
      }
      return sum;
    }

    public int totalNumberOfHereDoc(List<LiteralExprTreeImpl> exprTrees){
      return filterHereDocFromLiteralExpressions(exprTrees).size();
    }

    public List<LiteralExprTreeImpl> filterHereDocFromLiteralExpressions(List<LiteralExprTreeImpl> wholeSet) {
      List<LiteralExprTreeImpl> hereDocs = new ArrayList<>();
      for (LiteralExprTreeImpl literalExprTree: wholeSet){
        if (this.isHereDocHere(literalExprTree)) {
          hereDocs.add(literalExprTree);
        }
      }
      return hereDocs;
    }

    public double avgNumberLinesPerHereDoc(List<LiteralExprTreeImpl> exprTrees) {
      int totalNumberOfHereDoc = totalNumberOfHereDoc(exprTrees);
      if (totalNumberOfHereDoc >= 1) {
        double avgNumberLinesPerHereDoc = (double) totalLinesOfHereDoc(exprTrees) / totalNumberOfHereDoc;
        BigDecimal roundedAverage = new BigDecimal(avgNumberLinesPerHereDoc).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberLinesPerHereDoc(List<LiteralExprTreeImpl> exprTrees) {
      List<LiteralExprTreeImpl> hereDocs = filterHereDocFromLiteralExpressions(exprTrees);

      if (hereDocs.isEmpty()){ return 0; }

      int max = numLinesPerHereDoc(hereDocs.get(0));

      for (LiteralExprTreeImpl expr: hereDocs) {
        int numberOfLinesPerHereDoc = numLinesPerHereDoc(expr);
        if (numberOfLinesPerHereDoc > max) {
          max = numberOfLinesPerHereDoc;
        }
      }
      return max;
    }

}

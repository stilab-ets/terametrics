package org.stilab.metrics.counter.block_level;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LiteralExpressionHereDocIdentifier {

    public LiteralExpressionHereDocIdentifier() {}

    private Matcher getHereDocMatcher(LiteralExprTreeImpl exprTree) {
      return Pattern.compile("<<([A-Z_]+)").matcher(exprTree.value());
    }

    public boolean isHereDocHere(LiteralExprTreeImpl exprTree){
      Matcher matcher = getHereDocMatcher(exprTree);
      return matcher.find();
    }

    public int numLinesPerHereDoc(LiteralExprTreeImpl exprTree) {
        String content = exprTree.value();
        Matcher matcher = getHereDocMatcher(exprTree);
        if (matcher.find()) {
          String delimiter = matcher.group(1);
          int startPos = matcher.end();
          int endPos = content.indexOf(delimiter, startPos);
          if (endPos != -1) {
            // Extract the heredoc content
            String heredocContent = content.substring(startPos, endPos);
            String[] lines = heredocContent.split("\n");
            return lines.length + 1;
          }
        }
      return 0;
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
        return (double) totalLinesOfHereDoc(exprTrees) / totalNumberOfHereDoc;
      }
      return 0.0;
    }

    public int maxNumberLinesPerHereDoc(List<LiteralExprTreeImpl> exprTrees) {
      List<LiteralExprTreeImpl> hereDocs = filterHereDocFromLiteralExpressions(exprTrees);
      int max = 0;
      for (LiteralExprTreeImpl expr: hereDocs) {
        int numberOfLinesPerHereDoc = numLinesPerHereDoc(expr);
        if (numberOfLinesPerHereDoc >= max) {
          max = numberOfLinesPerHereDoc;
        }
      }
      return max;
    }

}

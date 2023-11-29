package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.parser.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LiteralExpressionVisitor {
    private List<LiteralExprTreeImpl> literalExprTrees = new ArrayList<>();

    public List<LiteralExprTreeImpl> visit(AttributeTreeImpl attributeTree) {
      ExpressionTree expressionTree = attributeTree.value();
      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
      return trees.stream()
        .filter(LiteralExprTreeImpl.class::isInstance)
        .map(LiteralExprTreeImpl.class::cast)
        .collect(Collectors.toList());
    }

    public List<LiteralExprTreeImpl> filterLiteralExprFromAttributesList(List<AttributeTreeImpl>
                                                                           attributeTrees) {
      List<LiteralExprTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.visit(attributeAccess) );
      }
      return attributeAccessTrees;
    }

    public List<LiteralExprTreeImpl> filterLiteralExprFromBlock(BlockTreeImpl blockTree) {
      literalExprTrees = this.filterLiteralExprFromAttributesList((new AttrFinderImpl()).getAllAttributes(blockTree));
      return literalExprTrees;
    }

    public int totalNumberOfLiteralExpressions(){
        return literalExprTrees.size();
    }

    private boolean isNumeric(String str) {
      try {
        Double.parseDouble(str);
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
    }

    public boolean isLiteralExprValueString(LiteralExprTreeImpl literalExprTree) {
      return !literalExprTree.token().value().equals("true") &&
        !literalExprTree.token().value().equals("false") &&
        !literalExprTree.token().value().equals("null") &&
        !isNumeric(literalExprTree.token().value());
    }

    public int measureLengthOfStringLiteralExpr(LiteralExprTreeImpl literalExprTree) {
      if (isLiteralExprValueString(literalExprTree)) {
        return literalExprTree.value().length();
      }
      return 0;
    }

    public int numStringValues(){
      int sum = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        if (isLiteralExprValueString(literalExprTree)) {
          sum += 1;
        }
      }
      return sum;
    }

    public int sumLengthOfStringLiteralExpr() {
      int sum = 0;
      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        sum += measureLengthOfStringLiteralExpr(literalExprTree);
      }
      return sum;
    }

    public int maxLengthOfStringLiteralExpr() {

      if (literalExprTrees.isEmpty()){ return 0; }

      int max = measureLengthOfStringLiteralExpr(literalExprTrees.get(0));

      for (LiteralExprTreeImpl literalExprTree: literalExprTrees) {
        int value = measureLengthOfStringLiteralExpr(literalExprTree);
        if (value > max) {
          max = value;
        }
      }

      return max;
    }

    public double avgLengthOfStringLiteralExpr() {
      int domino = numStringValues();
      if (domino!=0) {
        double avgLengthOfStringLiteralExpr = (double) sumLengthOfStringLiteralExpr() / domino;
        BigDecimal roundedAverage = BigDecimal.valueOf(avgLengthOfStringLiteralExpr).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

}

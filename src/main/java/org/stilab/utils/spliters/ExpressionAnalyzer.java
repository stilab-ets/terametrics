package org.stilab.utils.spliters;

import org.sonar.iac.common.api.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class ExpressionAnalyzer {
  private static ExpressionAnalyzer instance;

  private ExpressionAnalyzer() {}

  public static ExpressionAnalyzer getInstance() {
    if (instance == null) {
      synchronized (ExpressionAnalyzer.class) {
        if (instance == null) {
          instance = new ExpressionAnalyzer();
        }
      }
    }
    return instance;
  }

  public List<Tree> getAllNestedExpressions(Tree expressionTree) {
    List<Tree> nestedExpressions = new ArrayList<>();
    if (expressionTree != null) {
      nestedExpressions.add( expressionTree );
      List<Tree> children = expressionTree.children();
//      System.out.println(children);
      for (Tree child : children) {
        nestedExpressions.addAll( getAllNestedExpressions(child)  );
      }
    }
    return nestedExpressions;
  }

}

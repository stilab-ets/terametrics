package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.SpecialStringCalculator;
import org.stilab.visitors.LiteralExpressionVisitor;
import org.stilab.visitors.SpecialStringVisitor;

public class SpecialStringCollector implements Decorator {


  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

//      LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();
//      SpecialStringVisitor specialStringVisitor = new SpecialStringVisitor(literalExpressionVisitor);

      SpecialStringCalculator specialStringCalculator = new SpecialStringCalculator(identifiedBlock);

      metrics.put("numEmptyString", specialStringCalculator.numberOfEmptyString());
      metrics.put("numWildCardSuffixString", specialStringCalculator.numberOfWildCardSuffixString());
      metrics.put("numStarString", specialStringCalculator.numberOfStarString());
      return metrics;
    }
}

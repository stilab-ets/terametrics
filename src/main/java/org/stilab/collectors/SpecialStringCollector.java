package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.LiteralExpressionVisitor;
import org.stilab.visitors.SpecialStringVisitor;

public class SpecialStringCollector implements Repository {


  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      LiteralExpressionVisitor literalExpressionVisitor = new LiteralExpressionVisitor();

      SpecialStringVisitor specialStringVisitor = new SpecialStringVisitor(literalExpressionVisitor);

      metrics.put("numEmptyString", specialStringVisitor.numberOfEmptyString(identifiedBlock));
      metrics.put("numWildCardSuffixString", specialStringVisitor.numberOfWildCardSuffixString(identifiedBlock));
      metrics.put("numStarString", specialStringVisitor.numberOfStarString(identifiedBlock));
      return metrics;
    }
}

package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.LiteralExpressionVisitor;
import org.stilab.metrics.visitors.SpecialStringVisitor;

public class SpecialStringRepository implements Repository {


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

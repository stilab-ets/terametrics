package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.MetaArgumentCalculator;
import org.stilab.visitors.MetaArgumentVisitor;

public class MetaArgumentCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

//    MetaArgumentVisitor metaArgumentVisitor = new MetaArgumentVisitor();

//    metaArgumentVisitor.filterMetaArguments(identifiedBlock);
//    int numMetaArg = metaArgumentVisitor.metaArgsCount();

    MetaArgumentCalculator metaArgumentCalculator = new MetaArgumentCalculator(identifiedBlock);
    metrics.put("numMetaArg", metaArgumentCalculator.countMetaArgs());

    return metrics;
  }
}

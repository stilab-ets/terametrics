package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.MetaArgumentVisitor;

public class MetaArgumentRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    MetaArgumentVisitor metaArgumentVisitor = new MetaArgumentVisitor();

    metaArgumentVisitor.filterMetaArguments(identifiedBlock);
    int numMetaArg = metaArgumentVisitor.metaArgsCount();
    metrics.put("numMetaArg", numMetaArg);

    return metrics;
  }
}

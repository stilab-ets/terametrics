package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.AttrFinderImpl;

public class AttributesCollector implements Decorator {
  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    AttrFinderImpl attrFinder = new AttrFinderImpl();
    metrics.put("numAttrs", attrFinder.getAllAttributes(identifiedBlock).size());
    return metrics;

  }
}

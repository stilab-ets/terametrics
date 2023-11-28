package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.AttrFinderImpl;

public class AttributesRepository implements Repository{
  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    AttrFinderImpl attrFinder = new AttrFinderImpl();
    metrics.put("numAttrs", attrFinder.getAllAttributes(identifiedBlock).size());
    return metrics;

  }
}

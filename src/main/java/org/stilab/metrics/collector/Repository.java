package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Repository {
  JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock);
}

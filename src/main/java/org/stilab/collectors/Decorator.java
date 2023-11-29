package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Decorator {
  JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock);
}

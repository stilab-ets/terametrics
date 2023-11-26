package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Repository {
  JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock);
}

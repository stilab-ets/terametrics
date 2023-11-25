package org.stilab.metrics.counter.block.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

public interface Repository {
  JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock);
}

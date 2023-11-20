package org.stilab.metrics.counter.block.metrics.deprecation.cloud;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeprecatoryServiceLocator {
  public List<Deprecation> deprecations;
    private String blockAsString;
    private BlockTreeImpl identifiedBlock;

    public DeprecatoryServiceLocator(BlockTreeImpl identifiedBlock, String blockAsString) {
      this.identifiedBlock = identifiedBlock;
      this.blockAsString = blockAsString;
      this.deprecations = new ArrayList<>( Arrays.asList(
                          // AWS Context
                          new DeprecatedDataSource(
                            "deprecated_data_source_aws.json",
                            identifiedBlock,
                            blockAsString),

                          new DeprecatedProvider("deprecated_provider_aws.json",
                            identifiedBlock,
                            blockAsString),

                          new DeprecatedResource("deprecated_resource_aws.json",
                            identifiedBlock,
                            blockAsString)
      ));
    }

    public void addDeprecation(Deprecation deprecation) {
      this.deprecations.add(deprecation);
    }

    public int countDeprecationWithinBlock(){
      int depr = 0;
      for (Deprecation deprecation1: this.deprecations){
        depr += deprecation1.countDeprecation();
      }
      return depr;
    }

    public JSONObject updateMetrics(JSONObject metric, BlockTreeImpl identifiedBlock) {
      int numDeprecation = this.countDeprecationWithinBlock();
      metric.put("numDeprecatedKeywords", numDeprecation);
      return metric;
    }
}

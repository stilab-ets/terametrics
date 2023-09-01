package org.stilab.metrics.counter.block_level.deprecation.cloud;

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
//                          new DeprecatedDataSource(
//                            "src/main/resources/deprecation/cloud/aws/blocks/datasource/deprecated_data_source_aws.json",
//                            identifiedBlock,
//                            blockAsString),

                          new DeprecatedProvider("src/main/resources/deprecation/cloud/aws/blocks/provider/deprecated_provider_aws.json",
                            identifiedBlock,
                            blockAsString),

                          new DeprecatedResource("src/main/resources/deprecation/cloud/aws/blocks/resource/deprecated_resource_aws.json",
                            identifiedBlock,
                            blockAsString)
                          //
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
}

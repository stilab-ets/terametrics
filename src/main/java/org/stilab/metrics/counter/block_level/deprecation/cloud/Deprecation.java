package org.stilab.metrics.counter.block_level.deprecation.cloud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Deprecation {
  protected ObjectMapper deprecatedDataSourcesMapper = new ObjectMapper();
  protected List<Block> deprecatedBlocks = new ArrayList<>();
  protected String blockAsString;

  protected BlockTreeImpl block;

  public Deprecation(String filePath, BlockTreeImpl block, String blockAsString) {

    try {
      this.block = block;

      File jsonFile = new File(filePath);

      this.blockAsString = blockAsString;
      this.deprecatedBlocks = this.deprecatedDataSourcesMapper.readValue(jsonFile, new TypeReference<List<Block>>() {});

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public int countDeprecation() {
    return 0;
  }
}

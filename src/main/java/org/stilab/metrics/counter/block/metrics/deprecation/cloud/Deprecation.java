package org.stilab.metrics.counter.block.metrics.deprecation.cloud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.utils.mapper.Block;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Deprecation {
  protected ObjectMapper deprecatedDataSourcesMapper = new ObjectMapper();
  protected List<Block> deprecatedBlocks = new ArrayList<>();
  protected String blockAsString;
  protected BlockTreeImpl block;

  public Deprecation(String filePath, BlockTreeImpl block, String blockAsString) {
    this.block = block;
    this.blockAsString = blockAsString;

    try {
      // Load the resource as an InputStream
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

      if (inputStream != null) {
        readDataFromInputStream(inputStream);
      } else {
        System.err.println("Resource not found: " + filePath);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readDataFromInputStream(InputStream inputStream) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      // Read the content from the InputStream
      StringBuilder contentBuilder = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        contentBuilder.append(line).append('\n');
      }

      // Parse the JSON content into the deprecatedBlocks list
      String jsonContent = contentBuilder.toString();
      deprecatedBlocks = deprecatedDataSourcesMapper.readValue(jsonContent, new TypeReference<List<Block>>() {});
    }
  }

  public int countDeprecation() {
    return deprecatedBlocks.size(); // Adjust as needed based on your counting logic
  }
}


package org.stilab.visitors.deprecation.cloud;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.parser.mapper.Block;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Deprecation {
  private static final Logger logger = Logger.getLogger(Deprecation.class.getName());
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
        logger.log(Level.SEVERE, "Error while reading objects to file {0} ", filePath);
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


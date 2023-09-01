package org.stilab.block;

import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block.metrics.BlockComplexity;
import org.stilab.metrics.counter.block.metrics.deprecation.cloud.DeprecatedDataSource;
import org.stilab.metrics.counter.block.metrics.deprecation.cloud.DeprecatedResource;
import org.stilab.metrics.counter.block.metrics.deprecation.cloud.DeprecatoryServiceLocator;

import java.io.File;
import java.util.List;

public class DeprecationTest extends TestCase {
  private BlockTreeImpl identifiedBlock;
  private JSONObject metrics;
  private BlockComplexity blockComplexity;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    String filePath = "src/test/java/org/stilab/block/data/deprecation.tf";
    File base = new File(filePath);
    HclParser hclParser = new HclParser();
    Tree tree = hclParser.parse(base);
    // To look for the Parsed One
    TopBlockFinder topBlockFinder = new TopBlockFinder();
    List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
    identifiedBlock = blocks.get(0);
    metrics = new JSONObject();
    blockComplexity = new BlockComplexity(filePath, identifiedBlock);
  }

  public void testDataSourceDeprecation() {
    String filePath = "src/main/resources/deprecation/cloud/aws/blocks/datasource/deprecated_data_source_aws.json";
    String blockAsString = this.blockComplexity.getBlockContent();
    DeprecatedDataSource deprecatedDataSource = new DeprecatedDataSource(filePath, identifiedBlock, blockAsString);
    assertEquals(deprecatedDataSource.countDeprecation(), 0);
  }

  public void testResourceDeprecation() {
    String filePath = "src/main/resources/deprecation/cloud/aws/blocks/resource/deprecated_resource_aws.json";
    String blockAsString = this.blockComplexity.getBlockContent();
    DeprecatedResource deprecatedResource = new DeprecatedResource(filePath, identifiedBlock, blockAsString);
    assertEquals(deprecatedResource.countDeprecation(), 3);
  }

  public void testDeprecatoryServiceLocator() {
    String blockAsString = this.blockComplexity.getBlockContent();
    DeprecatoryServiceLocator deprecatoryServiceLocator = new DeprecatoryServiceLocator(identifiedBlock, blockAsString);
    assertEquals(deprecatoryServiceLocator.countDeprecationWithinBlock(), 3);
  }



}

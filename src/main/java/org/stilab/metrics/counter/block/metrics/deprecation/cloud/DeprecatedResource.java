package org.stilab.metrics.counter.block.metrics.deprecation.cloud;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.checker.BlockCheckerTypeImpl;
import org.stilab.utils.mapper.Block;
import org.stilab.utils.mapper.DeprecatedAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeprecatedResource extends Deprecation {
  public DeprecatedResource(String filePath, BlockTreeImpl block, String blockAsString) {
    super(filePath, block, blockAsString);
  }

  public List<String> getDeprecatedUsedResources() {
    List<String> resources = new ArrayList<>();
    BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();

    for (Block block : this.deprecatedBlocks) {
      String blockName = block.getBlockName();
      List<DeprecatedAttribute> attributes = block.getDeprecatedAttributes();

      if (block.getIsDeprecated()) {
        String p = "\\b" + blockName + "\\b";
        resources.add(p);
      }

      if (!attributes.isEmpty()) {
        for (DeprecatedAttribute attribute : attributes) {
          if (attribute.getType().equals("arg")) {
            String p1 = "\\b" + blockName + "\\.[\\w\\d_]+\\." + attribute.getName() + "\\b";
            resources.add(p1);

            if (blockCheckerType.isResource(this.block) && this.block.labels().get(0).value().equals(blockName)) {
              String p2 = "\\b" + attribute.getName() + "\\b";
              resources.add(p2);
            }

          } else if (attribute.getType().equals("nested")) {
            String p3 = "\\b" + blockName + "\\.[\\w\\d_]+" + attribute.getParent() + "\\." + attribute.getName() + "\\b";
            resources.add(p3);

            if (blockCheckerType.isResource(this.block) && this.block.labels().get(0).value().equals(blockName)) {
              String p4 = "\\b" + attribute.getName() + "\\b";
              resources.add(p4);
            }
          }
        }
      }
    }
    return resources;
  }

  @Override
  public int countDeprecation(){
    Set<String> resourcePatterns = new HashSet<>();
    resourcePatterns.addAll(this.getDeprecatedUsedResources());
    int resourceDeprecation = 0;

    for (String patternString :resourcePatterns) {
      // Compile the pattern
      Pattern pattern = Pattern.compile(patternString);
      // Find matches in the blockAsString
      Matcher matcher = pattern.matcher(blockAsString);
      while (matcher.find()) {
        resourceDeprecation +=1;
        // Print the matched content
      }
    }
    return resourceDeprecation;
  }

}

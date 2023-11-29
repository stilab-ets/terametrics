package org.stilab.visitors.deprecation.cloud;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.BlockCheckerTypeVisitor;
import org.stilab.parser.mapper.Block;
import org.stilab.parser.mapper.DeprecatedAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeprecatedProvider extends Deprecation {
  public DeprecatedProvider(String filePath, BlockTreeImpl block, String blockAsString) {
    super(filePath, block, blockAsString);
  }

  public List<String> generateProviderDeprecationPattern(){
    List<String> providerAttrs = new ArrayList<>();
    BlockCheckerTypeVisitor blockCheckerType = new BlockCheckerTypeVisitor();

    for (Block block: this.deprecatedBlocks) {
      String blockName = block.getBlockName();
      List<DeprecatedAttribute> attributes = block.getDeprecatedAttributes();

      if (block.getIsDeprecated()) {
        String p1 = "\\b" + blockName + "\\." + "\\b";
        providerAttrs.add(p1);
      }

      if (!attributes.isEmpty()) {
        for (DeprecatedAttribute attribute: attributes) {
          if (attribute.getType().equals("arg")) {
            String p2 = "\\b" + blockName + "\\.[\\w\\d_]+\\." + attribute.getName() + "\\b";
            providerAttrs.add(p2);

            if (blockCheckerType.isProvider(this.block) && this.block.labels().get(0).value().equals(blockName)) {
              String p3 = "\\b" + attribute.getName() + "\\b";
              providerAttrs.add(p3);
            }
          } else if (attribute.getType().equals("nested")) {

            String p4 = "\\b" + blockName + "\\.[\\w\\d_]+\\." + attribute.getParent() + "\\." +
              attribute.getName() + "\\b";
            providerAttrs.add(p4);

            if (blockCheckerType.isData(this.block) && this.block.labels().get(0).value().equals(blockName)) {
              String p5 = "\\b" + attribute.getName() + "\\b";
              providerAttrs.add(p5);
            }
          }
        }
      }
    }

    return providerAttrs;
  }

  @Override
  public int countDeprecation() {
    Set<String> providerPatterns = new HashSet<>();
    providerPatterns.addAll(this.generateProviderDeprecationPattern());
    int providerDeprecation = 0;

    for (String patternString: providerPatterns) {
      // Compile the pattern
      Pattern pattern = Pattern.compile(patternString);
      // Find matches in the blockAsString
      Matcher matcher = pattern.matcher(blockAsString);

      while (matcher.find()) {
        providerDeprecation +=1;
      }
    }
    return providerDeprecation;
  }

}

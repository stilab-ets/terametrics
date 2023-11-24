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


public class DeprecatedDataSource extends Deprecation {

  public DeprecatedDataSource(String filePath, BlockTreeImpl block, String blockAsString) {
    super(filePath, block, blockAsString);
  }

  public List<String> getDeprecatedDataSourcesUsedInBlock() {

      List<String> dataSources = new ArrayList<>();
      BlockCheckerTypeImpl blockCheckerType = new BlockCheckerTypeImpl();

      for (Block block: this.deprecatedBlocks) {
        String topLevelPrefix = "(?!data\\.)";
        String blockName = block.getBlockName();
        List<DeprecatedAttribute> attributes = block.getDeprecatedAttributes();

        if (block.getIsDeprecated()) {
          String p1 = "\\b" + blockName + "\\b";
          dataSources.add(p1);
        }

        if (!attributes.isEmpty()) {
          for (DeprecatedAttribute attribute: attributes) {
            if (attribute.getType().equals("arg")) {
              String p2 = topLevelPrefix + "\\." + blockName + "\\.[\\w\\d_]+\\." + attribute.getName() + "\\b";
              dataSources.add(p2);

              if (blockCheckerType.isData(this.block) && this.block.labels().get(0).value().equals(blockName)) {
                String p3 = "\\b" + attribute.getName() + "\\b";
                dataSources.add(p3);
              }

            } else if (attribute.getType().equals("nested")) {
              String p4 = topLevelPrefix + "\\." + blockName + "\\.[\\w\\d_]+\\." + attribute.getParent() + "\\." +
                attribute.getName() + "\\b";
              dataSources.add(p4);

              if (blockCheckerType.isData(this.block) && this.block.labels().get(0).value().equals(blockName)) {
                String p5 = "\\b" + attribute.getName() + "\\b";
                dataSources.add(p5);
              }
            }
          }
        }
      }
      return dataSources;
  }

  @Override
  public int countDeprecation(){
      Set<String> dataSourcePatterns = new HashSet<>();
      dataSourcePatterns.addAll(this.getDeprecatedDataSourcesUsedInBlock());
      int dataSourceDeprecation = 0;

      for (String patternString : dataSourcePatterns) {

        // Compile the pattern
        Pattern pattern = Pattern.compile(patternString);

        // Find matches in the blockAsString
        Matcher matcher = pattern.matcher(blockAsString);

        while (matcher.find()) {
          dataSourceDeprecation +=1;
        }
      }
      return dataSourceDeprecation;
  }


}

package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.sonar.iac.terraform.api.tree.BlockTree;
import org.sonar.iac.terraform.api.tree.SyntaxToken;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.block.counter.NestedBlockIdentifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaArgumentIdentifier {

      private List<TerraformTreeImpl> terraformTrees = new ArrayList<>();
      private List<String> meta_args_attributes = new ArrayList<>(Arrays.asList("depends_on", "count", "for_each", "provider"));
      private List<String> meta_args_blocks = new ArrayList<>(Arrays.asList("lifecycle", "provisioner", "connection"));
      public MetaArgumentIdentifier() { }

      // From attribute
      public boolean isMetaArgumentFromAttribute(AttributeTreeImpl attribute) {
        SyntaxToken token = attribute.key();
        return this.meta_args_attributes.contains(token.value());
      }

      // From Block
      public boolean isMetaArgumentFromBlock(BlockTree nestedBlock) {
        SyntaxToken token = nestedBlock.key();
        return this.meta_args_blocks.contains(token.value());
      }

      public void filterMetaArgumentsFromAttributesList( List<AttributeTreeImpl> attributeTrees ){
        for (AttributeTreeImpl attributeTree: attributeTrees) {
          if ( isMetaArgumentFromAttribute(attributeTree) ) {
            terraformTrees.add(attributeTree);
          }
        }
      }

      public void filterMetaArgumentsFromBlocksList(List<BlockTreeImpl> blockTrees) {
        for (BlockTreeImpl blockTree1: blockTrees) {
          if ( isMetaArgumentFromBlock(blockTree1) ) {
            terraformTrees.add(blockTree1);
          }
        }
      }

      public List<TerraformTreeImpl> filterMetaArguments(BlockTreeImpl blockTree) {

    //    Treats the attributes  !!!
        List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAttributes(blockTree);
        this.filterMetaArgumentsFromAttributesList(attributeTrees);
    //    Treats the related Blocks !!!
        NestedBlockIdentifier nestedBlockCounter = new NestedBlockIdentifier();
        List<BlockTreeImpl> blockTreeList = new ArrayList<>();
        blockTreeList.addAll(nestedBlockCounter.identifyNestedBlock(blockTree));
        this.filterMetaArgumentsFromBlocksList(blockTreeList);

        return terraformTrees;
      }

      public int meta_args_count() {  return this.terraformTrees.size();  }

      public JSONObject updateMetrics(JSONObject metrics, BlockTreeImpl identifiedBlock) {

        this.filterMetaArguments(identifiedBlock);
        int numMetaArg = this.meta_args_count();
        metrics.put("numMetaArg", numMetaArg);

        return metrics;
      }
}

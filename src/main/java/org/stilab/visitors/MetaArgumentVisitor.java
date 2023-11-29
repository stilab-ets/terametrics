package org.stilab.visitors;

import org.sonar.iac.terraform.api.tree.BlockTree;
import org.sonar.iac.terraform.api.tree.SyntaxToken;
import org.sonar.iac.terraform.tree.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaArgumentVisitor {

      private List<TerraformTreeImpl> terraformTrees = new ArrayList<>();
      private List<String> metaArgsAttributes = new ArrayList<>(Arrays.asList("depends_on", "count", "for_each", "provider"));
      private List<String> metaArgsBlocks = new ArrayList<>(Arrays.asList("lifecycle", "provisioner", "connection"));

      // From attribute
      public boolean visit(AttributeTreeImpl attribute) {
        SyntaxToken token = attribute.key();
        return this.metaArgsAttributes.contains(token.value());
      }

      // From Block
      public boolean isMetaArgumentFromBlock(BlockTree nestedBlock) {
        SyntaxToken token = nestedBlock.key();
        return this.metaArgsBlocks.contains(token.value());
      }

      public void filterMetaArgumentsFromAttributesList( List<AttributeTreeImpl> attributeTrees ){
        for (AttributeTreeImpl attributeTree: attributeTrees) {
          if ( visit(attributeTree) ) {
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
        NestedBlockVisitor nestedBlockCounter = new NestedBlockVisitor();
        List<BlockTreeImpl> blockTreeList = new ArrayList<>();
        blockTreeList.addAll(nestedBlockCounter.identifyNestedBlock(blockTree));
        this.filterMetaArgumentsFromBlocksList(blockTreeList);

        return terraformTrees;
      }

      public int metaArgsCount() {  return this.terraformTrees.size();  }

}

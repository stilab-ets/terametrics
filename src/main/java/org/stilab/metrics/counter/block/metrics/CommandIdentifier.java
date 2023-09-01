package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import java.util.ArrayList;
import java.util.List;

public class CommandIdentifier {

   public List<AttributeTreeImpl> commands = new ArrayList<>();

   public boolean isCommand(AttributeTreeImpl attributeTree) {
     return attributeTree.key().value().equals("command");
   }

   public List<AttributeTreeImpl> identifyCommandsAttributes(List<AttributeTreeImpl> attributes) {

     List<AttributeTreeImpl> commandAttrs = new ArrayList<>();

     for (AttributeTreeImpl attribute: attributes) {
        if (isCommand(attribute)) {
          commandAttrs.add(attribute);
        }
     }

     return commandAttrs;
   }

   public List<AttributeTreeImpl> identifyCommandsFromBlock(BlockTreeImpl blockTree) {
     List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl()).getAllAttributes(blockTree);
     this.commands = this.identifyCommandsAttributes(attributeTrees);
     return this.commands;
   }

   public int countCommandsPerBlock() {
     return this.commands.size();
   }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      this.identifyCommandsFromBlock(identifiedBlock);
      int numCommand = this.countCommandsPerBlock();
      metrics.put("numCommand", numCommand);
      return metrics;
    }
}

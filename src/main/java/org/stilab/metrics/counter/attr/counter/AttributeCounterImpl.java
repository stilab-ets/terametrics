package org.stilab.metrics.counter.attr.counter;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.interfaces.AttributeCounter;
import org.sonar.iac.common.api.tree.Tree;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import java.util.List;

public class AttributeCounterImpl implements AttributeCounter {

      @Override
    //  TerraformTree tree
    public int countAttribute(Tree tree) {
      List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
        .getAllAttributes((BlockTreeImpl) tree);
      return attributeTrees.size();
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      int attributes = this.countAttribute(identifiedBlock);
      metrics.put("numAttributes", attributes);
      return metrics;
    }
}

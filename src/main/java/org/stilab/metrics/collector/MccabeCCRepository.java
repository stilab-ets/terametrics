package org.stilab.metrics.collector;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.MccabeCC;

import java.util.List;

public class MccabeCCRepository implements Repository {

    @Override
    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){


      MccabeCC mccabeCC = new MccabeCC();

      List<AttributeTreeImpl> attributes = mccabeCC.getAllAttributes(identifiedBlock);
      double avgMccabeCC = mccabeCC.avgMccabeCC(attributes);
      int sumMccabeCC = mccabeCC.sumMccabeCC(attributes);
      int maxMccabeCC = mccabeCC.maxMccabeCC(attributes);

      metrics.put("avgMccabeCC", avgMccabeCC);
      metrics.put("sumMccabeCC", sumMccabeCC);
      metrics.put("maxMccabeCC", maxMccabeCC);

      return metrics;
    }

}

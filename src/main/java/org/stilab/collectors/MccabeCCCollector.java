package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.MccabeCC;

import java.util.List;

public class MccabeCCCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){


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

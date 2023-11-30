package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.MccabeCCCalculator;
import org.stilab.visitors.MccabeCC;

import java.util.List;

public class MccabeCCCollector implements Decorator {

    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      MccabeCCCalculator mccabeCCCalculator = new MccabeCCCalculator(identifiedBlock);

      double avgMccabeCC = mccabeCCCalculator.avgMccabeCC();
      int sumMccabeCC = mccabeCCCalculator.sumMccabeCC();
      int maxMccabeCC = mccabeCCCalculator.maxMccabeCC();

      metrics.put("avgMccabeCC", avgMccabeCC);
      metrics.put("sumMccabeCC", sumMccabeCC);
      metrics.put("maxMccabeCC", maxMccabeCC);

      return metrics;
    }

}

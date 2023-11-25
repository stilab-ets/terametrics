package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MccabeCC {

    AttrFinderImpl attributeFinder = new AttrFinderImpl();

    public int measureMccabeCCForAnAttributes(AttributeTreeImpl attributeTree) {

      List<TerraformTreeImpl> complexity = new ArrayList<>();

      // Get Number of Conditions
      ConditionalExpressionVisitor conditionalExpressionVisitor = new ConditionalExpressionVisitor();
      List<TerraformTreeImpl> conditions =  conditionalExpressionVisitor.visit(attributeTree);
      complexity.addAll(conditions);

      // Get Number of Loops
      LoopsExpressionVisitor loopsExpressionVisitor = new LoopsExpressionVisitor();
      List<TerraformTreeImpl> loops = loopsExpressionVisitor.visit(attributeTree);
      complexity.addAll(loops);

      return complexity.size() + 1;
    }

    public List<AttributeTreeImpl> getAllAttributes(BlockTreeImpl blockTree) {
      return this.attributeFinder.getAllAttributes(blockTree);
    }

    public int sumMccabeCC(List<AttributeTreeImpl> attributes) {
      int sum = 0;
      for (AttributeTreeImpl attribute: attributes) {
        sum += measureMccabeCCForAnAttributes(attribute);
      }
      return sum;
    }

    public double avgMccabeCC(List<AttributeTreeImpl> attributes) {
      if (!attributes.isEmpty()) {
        double avgMccabeCC = (double) sumMccabeCC(attributes) / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgMccabeCC).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxMccabeCC(List<AttributeTreeImpl> attributes){

      if (attributes.isEmpty()){ return 0;}

      int max = measureMccabeCCForAnAttributes(attributes.get(0));

      for (AttributeTreeImpl attribute: attributes) {
        int value = measureMccabeCCForAnAttributes(attribute);
        if (max < value) {
          max = value;
        }
      }

      return max;
    }

//    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
//
//      List<AttributeTreeImpl> attributes = getAllAttributes(identifiedBlock);
//      double avgMccabeCC = this.avgMccabeCC(attributes);
//      int sumMccabeCC = this.sumMccabeCC(attributes);
//      int maxMccabeCC = this.maxMccabeCC(attributes);
//
//      metrics.put("avgMccabeCC", avgMccabeCC);
//      metrics.put("sumMccabeCC", sumMccabeCC);
//      metrics.put("maxMccabeCC", maxMccabeCC);
//
//      return metrics;
//    }

}

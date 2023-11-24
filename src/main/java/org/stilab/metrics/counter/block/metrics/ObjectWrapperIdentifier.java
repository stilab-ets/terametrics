package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.utils.spliters.ExpressionAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectWrapperIdentifier {

    List<TerraformTreeImpl> objects = new ArrayList<>();
    List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<TerraformTreeImpl> identifyObjects(AttributeTreeImpl attributeTree) {

      ExpressionTree expressionTree = attributeTree.value();

      List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

      Stream<TerraformTreeImpl> objects = trees
        .stream()
        .filter(child -> child instanceof ObjectTreeImpl)
        .map(child -> (TerraformTreeImpl) child );

      Stream<TerraformTreeImpl> forObjects = trees
        .stream()
        .filter(child -> child instanceof ForObjectTreeImpl)
        .map(child -> (TerraformTreeImpl) child);

      Stream<TerraformTreeImpl> combinedFilters = Stream.concat(objects, forObjects);

      return combinedFilters.collect(Collectors.toList());
    }

    public List<TerraformTreeImpl> filterObjectsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
      List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
      for(AttributeTreeImpl attributeAccess: attributeTrees) {
        attributeAccessTrees.addAll( this.identifyObjects(attributeAccess) );
      }
      return attributeAccessTrees;
    }

    public List<TerraformTreeImpl> filterObjectsFromBlock(BlockTreeImpl blockTree) {
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      objects = this.filterObjectsFromAttributesList(attributes);
      return objects;
    }

    public int totalNumberOfObjects() {
      return objects.size();
    }

    public double avgNumberOfObjects() {
      if (!attributes.isEmpty()) {
        double avgNumberOfObjects = (double) totalNumberOfObjects() / attributes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(avgNumberOfObjects).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
      }
      return 0.0;
    }

    public int maxNumberOfObjects() {

      if (attributes.isEmpty()) { return 0;}
      int max = identifyObjects(attributes.get(0)).size();
      for (AttributeTreeImpl attribute: attributes) {
        int value = identifyObjects(attribute).size();
        if (value > max) {
          max = value;
        }
      }
      return max;

    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.filterObjectsFromBlock(identifiedBlock);
      int numObjects = this.totalNumberOfObjects();
      double avgObjects = this.avgNumberOfObjects();
      int maxObjects = this.maxNumberOfObjects();

      metrics.put("numObjects", numObjects);
      metrics.put("avgObjects", avgObjects);
      metrics.put("maxObjects", maxObjects);

      return metrics;
    }
}

package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.api.tree.SyntaxToken;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;

import java.util.ArrayList;
import java.util.List;

public class ExplicitResourceDependency {

    public List<AttributeTreeImpl> attributes = new ArrayList<>();
    public ExplicitResourceDependency() {}

    public int numberOfDependentResources(List<AttributeTreeImpl> attributes) {

      for (AttributeTreeImpl attribute: attributes) {
        SyntaxToken token = attribute.key();
        if (token.value().equals("depends_on")) {
          TupleIdentifier tupleIdentifier = new TupleIdentifier();
          List<TerraformTreeImpl> tuples = tupleIdentifier.filterTupleIdentifier(attribute);
          TupleElementsIdentifier tupleIdentifiers = new TupleElementsIdentifier(tuples);
          return tupleIdentifiers.getTotalNumberOfElementsOfDifferentTuples();
        }
      }
      return 0;
    }

    public int getNumberOfResourceDependency(BlockTreeImpl blockTree){
      attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
      return numberOfDependentResources(attributes);
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){
      metrics.put("numExplicitResourceDependency", getNumberOfResourceDependency(identifiedBlock));
      return metrics;
    }
}

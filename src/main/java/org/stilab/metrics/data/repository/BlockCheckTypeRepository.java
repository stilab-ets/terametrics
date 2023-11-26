package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.iterators.AttrFinderImpl;
import org.stilab.metrics.visitors.BlockCheckerTypeVisitor;

import java.util.List;

public class BlockCheckTypeRepository implements Repository{

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

    AttrFinderImpl attrFinder = new AttrFinderImpl();

    List<AttributeTreeImpl> attributes = attrFinder.getAllAttributes(identifiedBlock);

    BlockCheckerTypeVisitor blockCheckerTypeVisitor = new BlockCheckerTypeVisitor();

    boolean containDescriptionField = attributes.stream().anyMatch(attribute ->
      "description".equals(attribute.key().value()));

    metrics.put("containDescriptionField", containDescriptionField ? 1 : 0);

    // Is resource ++
    boolean isResource = blockCheckerTypeVisitor.isResource(identifiedBlock);
    metrics.put("isResource", isResource ? 1 : 0);

    // Is module ++
    boolean isModule =  blockCheckerTypeVisitor.isModule(identifiedBlock);
    metrics.put("isModule", isModule ? 1 : 0);

    // Is data ++
    boolean isData = blockCheckerTypeVisitor.isData(identifiedBlock);
    metrics.put("isData", isData ? 1 : 0);

    // Is terraform
    boolean isTerraform = blockCheckerTypeVisitor.isTerraform(identifiedBlock);
    metrics.put("isTerraform", isTerraform ? 1 : 0);

    // Is provider
    boolean isProvider = blockCheckerTypeVisitor.isProvider(identifiedBlock);
    metrics.put("isProvider", isProvider ? 1 : 0);

    // Is variable
    boolean isVariable = blockCheckerTypeVisitor.isVariable(identifiedBlock);
    metrics.put("isVariable", isVariable ? 1 : 0);

    // Is output
    boolean isOutput = blockCheckerTypeVisitor.isOutput(identifiedBlock);
    metrics.put("isOutput", isOutput ? 1 : 0);

    // Is locals ++
    boolean isLocals = blockCheckerTypeVisitor.isLocals(identifiedBlock);
    metrics.put("isLocals", isLocals ? 1 : 0);

    return metrics;
  }
}

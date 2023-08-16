package org.stilab.metrics.checker;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.interfaces.BlockCheckerType;
import org.sonar.iac.common.checks.TextUtils;
import org.sonar.iac.terraform.api.tree.BlockTree;

public class BlockCheckerTypeImpl implements BlockCheckerType {

  @Override
  public boolean isResource(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "resource"::equals).isTrue();
  }

  @Override
  public boolean isProvider(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "provider"::equals).isTrue();
  }

  @Override
  public boolean isOutput(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "output"::equals).isTrue();
  }

  @Override
  public boolean isData(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "data"::equals).isTrue();
  }

  @Override
  public boolean isModule(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "module"::equals).isTrue();
  }

  @Override
  public boolean isLocals(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "locals"::equals).isTrue();
  }

  @Override
  public boolean isVariable(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "variable"::equals).isTrue();
  }

  @Override
  public boolean isTerraform(BlockTree tree) {
    return TextUtils.matchesValue(tree.key(), "terraform"::equals).isTrue();
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {


    // Is resource ++
    boolean isResource = this.isResource(identifiedBlock);
    metrics.put("isResource", isResource ? 1 : 0);

    // Is module ++
    boolean isModule =  this.isModule(identifiedBlock);
    metrics.put("isModule", isModule ? 1 : 0);

    // Is data ++
    boolean isData = this.isData(identifiedBlock);
    metrics.put("isData", isData ? 1 : 0);

    // Is terraform
    boolean isTerraform = this.isTerraform(identifiedBlock);
    metrics.put("isTerraform", isTerraform ? 1 : 0);

    // Is provider
    boolean isProvider = this.isProvider(identifiedBlock);
    metrics.put("isProvider", isProvider ? 1 : 0);

    // Is variable
    boolean isVariable = this.isVariable(identifiedBlock);
    metrics.put("isVariable", isVariable ? 1 : 0);

    // Is output
    boolean isOutput = this.isOutput(identifiedBlock);
    metrics.put("isOutput", isOutput ? 1 : 0);

    // Is locals ++
    boolean isLocals = this.isLocals(identifiedBlock);
    metrics.put("isLocals", isLocals ? 1 : 0);

    return metrics;
  }

}

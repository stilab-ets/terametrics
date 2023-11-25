package org.stilab.metrics.counter.block.visitors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.counter.block.iterators.AttrFinderImpl;
import org.stilab.metrics.counter.interfaces.BlockCheckerType;
import org.sonar.iac.common.checks.TextUtils;
import org.sonar.iac.terraform.api.tree.BlockTree;

import java.util.List;

public class BlockCheckerTypeVisitor implements BlockCheckerType {

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





}

package org.stilab.utils.mapper;

import java.util.List;

public class Block {
  private boolean isDeprecated;
  private String blockName;
  private List<DeprecatedAttribute> deprecatedAttributes;

  public String getBlockName() {
    return blockName;
  }

  public List<DeprecatedAttribute> getDeprecatedAttributes() {
    return deprecatedAttributes;
  }

  public boolean getIsDeprecated() {
    return isDeprecated;
  }
}

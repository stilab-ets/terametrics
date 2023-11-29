package org.stilab.parser.mapper;

public class DeprecatedAttribute {
  private String type;
  private String name;
  private String parent; // Optional, for certain attributes

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public String getParent() {
    return parent;
  }
}

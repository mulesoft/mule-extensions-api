/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

/**
 * Represents a selected field in a query.
 *
 * @since 1.0
 */
public final class Field {

  /**
   * The name of the field
   */
  private String name;

  /**
   * The type of the field
   */
  private String type;

  public Field(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public Field(String name) {
    this(name, null);
  }

  /**
   * @return the name of the field.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the type of the field.
   */
  public String getType() {
    return type;
  }
}

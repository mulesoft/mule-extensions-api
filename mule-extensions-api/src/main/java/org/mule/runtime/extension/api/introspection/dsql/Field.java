/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.dsql;

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

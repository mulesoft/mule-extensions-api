/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

/**
 * Represents the queried entity in a DSQL query.
 *
 * @since 1.0
 */
public class EntityType {

  /**
   * The name of the entity
   */
  private String name;

  public EntityType(String typeName) {
    this.name = typeName;
  }

  /**
   * @return the name of the entity
   */
  public String getName() {
    return name;
  }

}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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

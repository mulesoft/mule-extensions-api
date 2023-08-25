/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Indicates that a complex parameter that would have a wrapper element under normal conditions, doesn't have a wrapper element
 * when represented in the DSL.
 * 
 * @since 1.4
 */
public class NoWrapperModelProperty implements ModelProperty {

  private static final long serialVersionUID = 1272321832347102892L;

  public static final String NAME = "noWrapper";

  /**
   * Use this instance for memory efficiency
   */
  public static final NoWrapperModelProperty INSTANCE = new NoWrapperModelProperty();

  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return {@code true} since DSL must be handled consistently even after a serialization/deserialization of the extension
   *         model.
   */
  @Override
  public boolean isPublic() {
    return true;
  }

}

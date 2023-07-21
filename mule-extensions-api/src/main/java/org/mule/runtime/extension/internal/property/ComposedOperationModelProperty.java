/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} for indicating that the owning operation is a composed operation.
 *
 * @since 1.5
 */
public class ComposedOperationModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "composedOperation";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}

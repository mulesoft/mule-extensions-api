/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} for indicating that the owning operation may not have a configuring transactionalAction.
 *
 * @since 1.5
 */
public class NoTransactionalActionModelProperty implements ModelProperty {

  @Override
  public String getName() {
    return "noTransactionalAction";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}

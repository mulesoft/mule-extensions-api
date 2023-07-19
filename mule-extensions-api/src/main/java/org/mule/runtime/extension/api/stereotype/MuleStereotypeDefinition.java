/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

import org.mule.api.annotation.NoExtend;

/**
 * Marker interface that identifies a given {@link StereotypeDefinition} as a Mule stereotype.
 *
 * @since 1.0
 */
@NoExtend
public abstract class MuleStereotypeDefinition implements StereotypeDefinition {

  public static final String NAMESPACE = "MULE";

  @Override
  public final String getNamespace() {
    return NAMESPACE;
  }
}

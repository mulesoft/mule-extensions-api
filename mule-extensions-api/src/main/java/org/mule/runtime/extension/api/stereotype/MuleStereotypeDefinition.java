/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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

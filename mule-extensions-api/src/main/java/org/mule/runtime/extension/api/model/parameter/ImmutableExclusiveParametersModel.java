/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import org.mule.runtime.api.meta.model.parameter.ExclusiveParametersModel;

import java.util.Set;

/**
 * Immutable implementation of {@link ExclusiveParametersModel}
 *
 * @since 1.0
 */
public final class ImmutableExclusiveParametersModel implements ExclusiveParametersModel {

  private final Set<String> exclusiveParameterNames;
  private final boolean isOneRequired;

  /**
   * Creates anew instance
   * 
   * @param exclusiveParameterNames the names of the mutually exclusive parameters
   * @param isOneRequired           whether one is required
   */
  public ImmutableExclusiveParametersModel(Set<String> exclusiveParameterNames, boolean isOneRequired) {
    this.exclusiveParameterNames = exclusiveParameterNames;
    this.isOneRequired = isOneRequired;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getExclusiveParameterNames() {
    return exclusiveParameterNames;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOneRequired() {
    return isOneRequired;
  }
}

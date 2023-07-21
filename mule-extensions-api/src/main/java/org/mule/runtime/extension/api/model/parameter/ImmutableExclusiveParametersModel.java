/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

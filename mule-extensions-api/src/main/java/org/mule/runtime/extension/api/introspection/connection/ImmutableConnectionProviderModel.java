/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.connection;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.parameter.AbstractParameterizedModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link ConnectionProviderModel}
 *
 * @since 1.0
 */
public class ImmutableConnectionProviderModel extends AbstractParameterizedModel implements ConnectionProviderModel {

  private final ConnectionManagementType connectionManagementType;

  /**
   * Creates a new instance with the given state
   *
   * @param name            the provider's name
   * @param description     the provider's description
   * @param parameterModels a {@link List} with the provider's {@link ParameterModel parameterModels}
   * @param connectionManagementType the type of connection management that the provider performs
   * @param modelProperties A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType} are {@code null}
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterModel> parameterModels,
                                          ConnectionManagementType connectionManagementType,
                                          Set<ModelProperty> modelProperties) {
    super(name, description, modelProperties, parameterModels);
    checkArgument(connectionManagementType != null, "connectionManagementType cannot be null");
    this.connectionManagementType = connectionManagementType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectionManagementType getConnectionManagementType() {
    return connectionManagementType;
  }
}

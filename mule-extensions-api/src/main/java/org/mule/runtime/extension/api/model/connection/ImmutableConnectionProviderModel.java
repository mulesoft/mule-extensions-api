/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.connection;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.connection.ConnectionManagementType;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.parameter.AbstractStereotypedModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable implementation of {@link ConnectionProviderModel}
 *
 * @since 1.0
 */
public class ImmutableConnectionProviderModel extends AbstractStereotypedModel implements ConnectionProviderModel {

  private final ConnectionManagementType connectionManagementType;
  private final Set<ExternalLibraryModel> externalLibraryModels;
  private final boolean supportsConnectivityTesting;
  private final boolean supportsXa;
  private Set<String> semanticTerms;

  /**
   * Creates a new instance with the given state
   *
   * @param name                        the provider's name
   * @param description                 the provider's description
   * @param parameterGroupModels        a {@link List} with the provider's {@link ParameterGroupModel parameter group models}
   * @param connectionManagementType    the type of connection management that the provider performs
   * @param supportsConnectivityTesting whether this provider supports connectivity testing or not
   * @param externalLibraryModels       a {@link Set} with the provider's {@link ExternalLibraryModel external libraries}
   * @param displayModel                a model which contains directive about how this provider is displayed in the UI
   * @param modelProperties             A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType}
   *                                  are {@code null}
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterGroupModel> parameterGroupModels,
                                          ConnectionManagementType connectionManagementType,
                                          boolean supportsConnectivityTesting,
                                          Set<ExternalLibraryModel> externalLibraryModels,
                                          DisplayModel displayModel,
                                          StereotypeModel stereotype,
                                          Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, connectionManagementType, supportsConnectivityTesting,
         externalLibraryModels,
         displayModel, stereotype, modelProperties, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                        the provider's name
   * @param description                 the provider's description
   * @param parameterGroupModels        a {@link List} with the provider's {@link ParameterGroupModel parameter group models}
   * @param connectionManagementType    the type of connection management that the provider performs
   * @param supportsConnectivityTesting whether this provider supports connectivity testing or not
   * @param externalLibraryModels       a {@link Set} with the provider's {@link ExternalLibraryModel external libraries}
   * @param displayModel                a model which contains directive about how this provider is displayed in the UI
   * @param modelProperties             A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType}
   *                                  are {@code null}
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterGroupModel> parameterGroupModels,
                                          ConnectionManagementType connectionManagementType,
                                          boolean supportsConnectivityTesting,
                                          Set<ExternalLibraryModel> externalLibraryModels,
                                          DisplayModel displayModel,
                                          StereotypeModel stereotype,
                                          Set<ModelProperty> modelProperties,
                                          DeprecationModel deprecationModel) {
    this(name, description, parameterGroupModels, connectionManagementType, supportsConnectivityTesting,
         externalLibraryModels,
         displayModel, stereotype, modelProperties, deprecationModel, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                        the provider's name
   * @param description                 the provider's description
   * @param parameterGroupModels        a {@link List} with the provider's {@link ParameterGroupModel parameter group models}
   * @param connectionManagementType    the type of connection management that the provider performs
   * @param supportsConnectivityTesting whether this provider supports connectivity testing or not
   * @param externalLibraryModels       a {@link Set} with the provider's {@link ExternalLibraryModel external libraries}
   * @param displayModel                a model which contains directive about how this provider is displayed in the UI
   * @param modelProperties             A {@link Set} of custom properties which extend this model
   * @param semanticTerms               a {@link Set} of semantic terms which describes the connection's meaning and effect
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType}
   *                                  are {@code null}
   * @since 1.4.0
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterGroupModel> parameterGroupModels,
                                          ConnectionManagementType connectionManagementType,
                                          boolean supportsConnectivityTesting,
                                          Set<ExternalLibraryModel> externalLibraryModels,
                                          DisplayModel displayModel,
                                          StereotypeModel stereotype,
                                          Set<ModelProperty> modelProperties,
                                          DeprecationModel deprecationModel,
                                          Set<String> semanticTerms) {
    this(name, description, parameterGroupModels, connectionManagementType, supportsConnectivityTesting, false,
         externalLibraryModels,
         displayModel, stereotype, modelProperties, deprecationModel, semanticTerms, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                        the provider's name
   * @param description                 the provider's description
   * @param parameterGroupModels        a {@link List} with the provider's {@link ParameterGroupModel parameter group models}
   * @param connectionManagementType    the type of connection management that the provider performs
   * @param supportsConnectivityTesting whether this provider supports connectivity testing or not
   * @param externalLibraryModels       a {@link Set} with the provider's {@link ExternalLibraryModel external libraries}
   * @param displayModel                a model which contains directive about how this provider is displayed in the UI
   * @param modelProperties             A {@link Set} of custom properties which extend this model
   * @param semanticTerms               a {@link Set} of semantic terms which describes the connection's meaning and effect
   * @param minMuleVersion              the min mule version of the connection provider
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType}
   *                                  are {@code null}
   * @since 1.5.0
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterGroupModel> parameterGroupModels,
                                          ConnectionManagementType connectionManagementType,
                                          boolean supportsConnectivityTesting,
                                          Set<ExternalLibraryModel> externalLibraryModels,
                                          DisplayModel displayModel,
                                          StereotypeModel stereotype,
                                          Set<ModelProperty> modelProperties,
                                          DeprecationModel deprecationModel,
                                          Set<String> semanticTerms,
                                          MuleVersion minMuleVersion) {
    this(name, description, parameterGroupModels, connectionManagementType, supportsConnectivityTesting, false,
         externalLibraryModels, displayModel, stereotype, modelProperties, deprecationModel, semanticTerms, minMuleVersion);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                        the provider's name
   * @param description                 the provider's description
   * @param parameterGroupModels        a {@link List} with the provider's {@link ParameterGroupModel parameter group models}
   * @param connectionManagementType    the type of connection management that the provider performs
   * @param supportsConnectivityTesting whether this provider supports connectivity testing or not
   * @param supportsXa                  whether this provider connections may participate in an XA transaction.
   * @param externalLibraryModels       a {@link Set} with the provider's {@link ExternalLibraryModel external libraries}
   * @param displayModel                a model which contains directive about how this provider is displayed in the UI
   * @param modelProperties             A {@link Set} of custom properties which extend this model
   * @param semanticTerms               a {@link Set} of semantic terms which describes the connection's meaning and effect
   * @param minMuleVersion              the min mule version of the connection provider
   * @throws IllegalArgumentException if {@code connectionProviderFactory}, {@code configurationType} or {@code connectionType}
   *                                  are {@code null}
   * @since 1.10.0
   */
  public ImmutableConnectionProviderModel(String name,
                                          String description,
                                          List<ParameterGroupModel> parameterGroupModels,
                                          ConnectionManagementType connectionManagementType,
                                          boolean supportsConnectivityTesting,
                                          boolean supportsXa,
                                          Set<ExternalLibraryModel> externalLibraryModels,
                                          DisplayModel displayModel,
                                          StereotypeModel stereotype,
                                          Set<ModelProperty> modelProperties,
                                          DeprecationModel deprecationModel,
                                          Set<String> semanticTerms,
                                          MuleVersion minMuleVersion) {
    super(name, description, parameterGroupModels, displayModel, stereotype, modelProperties, deprecationModel, minMuleVersion);
    checkArgument(connectionManagementType != null, "connectionManagementType cannot be null");
    this.connectionManagementType = connectionManagementType;
    this.externalLibraryModels = unmodifiableSet(externalLibraryModels);
    this.supportsConnectivityTesting = supportsConnectivityTesting;
    this.supportsXa = supportsXa;
    this.semanticTerms = semanticTerms != null ? unmodifiableSet(semanticTerms) : emptySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectionManagementType getConnectionManagementType() {
    return connectionManagementType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ExternalLibraryModel> getExternalLibraryModels() {
    return externalLibraryModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsConnectivityTesting() {
    return supportsConnectivityTesting;
  }

  @Override
  public boolean supportsXa() {
    return supportsXa;
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.4.0
   */
  @Override
  public Set<String> getSemanticTerms() {
    if (semanticTerms == null) {
      semanticTerms = emptySet();
    }
    return semanticTerms;
  }

  @Override
  public String toString() {
    return "ImmutableConnectionProviderModel{" +
        "connectionManagementType=" + connectionManagementType +
        ", externalLibraryModels=" + externalLibraryModels +
        ", supportsConnectivityTesting=" + supportsConnectivityTesting +
        ", semanticTerms=" + semanticTerms +
        "} " + super.toString();
  }
}

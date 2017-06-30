/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import static java.util.Optional.ofNullable;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.parameter.ValuesProviderModel;
import org.mule.runtime.extension.api.model.AbstractNamedImmutableModel;

import java.util.Optional;
import java.util.Set;

/**
 * Immutable implementation of {@link ParameterModel}
 *
 * @since 1.0
 */
public final class ImmutableParameterModel extends AbstractNamedImmutableModel implements ParameterModel {

  private final MetadataType type;
  private final boolean hasDynamicType;
  private final boolean required;
  private final boolean isConfigOverride;
  private final ValuesProviderModel valuesProviderModel;
  private final ExpressionSupport expressionSupport;
  private final Object defaultValue;
  private final ParameterRole role;
  private final ParameterDslConfiguration dslConfiguration;
  private final LayoutModel layoutModel;

  /**
   * Creates a new instance with the given state
   *
   * @param name              the parameter's name. Cannot be blank.
   * @param description       the parameter's description
   * @param type              the parameter's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType    if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param required          whether this parameter is required or not
   * @param expressionSupport the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
   * @param defaultValue      this parameter's default value
   * @param role           this parameter's purpose
   * @param dslConfiguration          a model which describes the DSL semantics for this parameter
   * @param displayModel      a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel       a model which contains directives about the parameter's layout in the UI
   * @param modelProperties   A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same time
   */
  public ImmutableParameterModel(String name,
                                 String description,
                                 MetadataType type,
                                 boolean hasDynamicType,
                                 boolean required,
                                 boolean isConfigOverride,
                                 ExpressionSupport expressionSupport,
                                 Object defaultValue,
                                 ParameterRole role,
                                 ParameterDslConfiguration dslConfiguration,
                                 DisplayModel displayModel,
                                 LayoutModel layoutModel,
                                 ValuesProviderModel valuesProviderModel,
                                 Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, modelProperties);

    this.type = type;
    this.required = required;
    this.expressionSupport = expressionSupport;
    this.defaultValue = defaultValue;
    this.role = role;
    this.hasDynamicType = hasDynamicType;
    this.dslConfiguration = dslConfiguration;
    this.layoutModel = layoutModel;
    this.isConfigOverride = isConfigOverride;
    this.valuesProviderModel = valuesProviderModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetadataType getType() {
    return type;
  }

  @Override
  public boolean hasDynamicType() {
    return hasDynamicType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequired() {
    return required;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOverrideFromConfig() {
    return isConfigOverride;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExpressionSupport getExpressionSupport() {
    return expressionSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue() {
    return defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParameterDslConfiguration getDslConfiguration() {
    return dslConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParameterRole getRole() {
    return role;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<LayoutModel> getLayoutModel() {
    return ofNullable(layoutModel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ValuesProviderModel> getValuesProviderModel() {
    return ofNullable(valuesProviderModel);
  }
}

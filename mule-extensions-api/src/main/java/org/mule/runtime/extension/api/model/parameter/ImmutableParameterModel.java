/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import static java.util.Collections.emptySet;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.ofNullable;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.FieldValueProviderModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.parameter.ValueProviderModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractNamedImmutableModel;

import java.util.List;
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
  private final boolean isComponentId;
  private final ValueProviderModel valueProviderModel;
  private List<FieldValueProviderModel> fieldValueProviderModels;
  private final ExpressionSupport expressionSupport;
  private final Object defaultValue;
  private final ParameterRole role;
  private final ParameterDslConfiguration dslConfiguration;
  private final LayoutModel layoutModel;
  private final List<StereotypeModel> allowedStereotypeModels;
  private final DeprecationModel deprecationModel;
  private Set<String> semanticTerms;

  /**
   * Creates a new instance with the given state
   *
   * @param name                    the parameter's name. Cannot be blank.
   * @param description             the parameter's description
   * @param type                    the parameter's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType          if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param required                whether this parameter is required or not
   * @param expressionSupport       the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
   * @param defaultValue            this parameter's default value
   * @param role                    this parameter's purpose
   * @param dslConfiguration        a model which describes the DSL semantics for this parameter
   * @param displayModel            a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel             a model which contains directives about the parameter's layout in the UI
   * @param allowedStereotypeModels A {@link Set} with the stereotypes of the allowed values
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same
   *                                  time
   */
  @Deprecated
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
                                 ValueProviderModel valueProviderModel,
                                 List<StereotypeModel> allowedStereotypeModels,
                                 Set<ModelProperty> modelProperties) {
    this(name, description, type, hasDynamicType, required, isConfigOverride,
         false, expressionSupport, defaultValue, role, dslConfiguration,
         displayModel, layoutModel, valueProviderModel, allowedStereotypeModels, modelProperties);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                    the parameter's name. Cannot be blank.
   * @param description             the parameter's description
   * @param type                    the parameter's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType          if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param required                whether this parameter is required or not
   * @param isConfigOverride        whether this parameter is a configuration override or not
   * @param isComponentId           whether this parameter serves as a {@link ComponentModel} ID or not
   * @param expressionSupport       the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
   * @param defaultValue            this parameter's default value
   * @param role                    this parameter's purpose
   * @param dslConfiguration        a model which describes the DSL semantics for this parameter
   * @param displayModel            a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel             a model which contains directives about the parameter's layout in the UI
   * @param allowedStereotypeModels A {@link Set} with the stereotypes of the allowed values
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same
   *                                  time
   */
  public ImmutableParameterModel(String name,
                                 String description,
                                 MetadataType type,
                                 boolean hasDynamicType,
                                 boolean required,
                                 boolean isConfigOverride,
                                 boolean isComponentId,
                                 ExpressionSupport expressionSupport,
                                 Object defaultValue,
                                 ParameterRole role,
                                 ParameterDslConfiguration dslConfiguration,
                                 DisplayModel displayModel,
                                 LayoutModel layoutModel,
                                 ValueProviderModel valueProviderModel,
                                 List<StereotypeModel> allowedStereotypeModels,
                                 Set<ModelProperty> modelProperties) {
    this(name, description, type, hasDynamicType, required, isConfigOverride, isComponentId, expressionSupport, defaultValue,
         role, dslConfiguration, displayModel, layoutModel, valueProviderModel, allowedStereotypeModels, modelProperties, null);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                    the parameter's name. Cannot be blank.
   * @param description             the parameter's description
   * @param type                    the parameter's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType          if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param required                whether this parameter is required or not
   * @param isConfigOverride        whether this parameter is a configuration override or not
   * @param isComponentId           whether this parameter serves as a {@link ComponentModel} ID or not
   * @param expressionSupport       the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
   * @param defaultValue            this parameter's default value
   * @param role                    this parameter's purpose
   * @param dslConfiguration        a model which describes the DSL semantics for this parameter
   * @param displayModel            a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel             a model which contains directives about the parameter's layout in the UI
   * @param valueProviderModel      a value provider model
   * @param allowedStereotypeModels A {@link Set} with the stereotypes of the allowed values
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @param deprecationModel        a {@link DeprecationModel} describing if the parameter is deprecated. A null value means it is
   *                                not deprecated.
   * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same
   *                                  time
   */
  public ImmutableParameterModel(String name,
                                 String description,
                                 MetadataType type,
                                 boolean hasDynamicType,
                                 boolean required,
                                 boolean isConfigOverride,
                                 boolean isComponentId,
                                 ExpressionSupport expressionSupport,
                                 Object defaultValue,
                                 ParameterRole role,
                                 ParameterDslConfiguration dslConfiguration,
                                 DisplayModel displayModel,
                                 LayoutModel layoutModel,
                                 ValueProviderModel valueProviderModel,
                                 List<StereotypeModel> allowedStereotypeModels,
                                 Set<ModelProperty> modelProperties,
                                 DeprecationModel deprecationModel) {
    this(name, description, type, hasDynamicType, required, isConfigOverride, isComponentId, expressionSupport,
         defaultValue, role, dslConfiguration, displayModel, layoutModel, valueProviderModel, allowedStereotypeModels,
         modelProperties, deprecationModel, null, emptyList());
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                     the parameter's name. Cannot be blank.
   * @param description              the parameter's description
   * @param type                     the parameter's {@link MetadataType}. Cannot be {@code null}
   * @param hasDynamicType           if the given {@code type} is of dynamic kind and has to be discovered during design time
   * @param required                 whether this parameter is required or not
   * @param isConfigOverride         whether this parameter is a configuration override or not
   * @param isComponentId            whether this parameter serves as a {@link ComponentModel} ID or not
   * @param expressionSupport        the {@link ExpressionSupport} that applies to {@code this} {@link ParameterModel}
   * @param defaultValue             this parameter's default value
   * @param role                     this parameter's purpose
   * @param dslConfiguration         a model which describes the DSL semantics for this parameter
   * @param displayModel             a model which contains directive about how the parameter is displayed in the UI
   * @param layoutModel              a model which contains directives about the parameter's layout in the UI
   * @param valueProviderModel       a value provider model
   * @param allowedStereotypeModels  A {@link Set} with the stereotypes of the allowed values
   * @param modelProperties          A {@link Set} of custom properties which extend this model
   * @param deprecationModel         a {@link DeprecationModel} describing if the parameter is deprecated. A null value means it
   *                                 is not deprecated.
   * @param semanticTerms            a {@link Set} of semantic terms which describe the parameter's meaning and effect
   * @param fieldValueProviderModels {@link List} of field value provider models.
   * @throws IllegalArgumentException if {@code required} is {@code true} and {@code defaultValue} is not {@code null} at the same
   *                                  time
   *
   * @since 1.4.0
   */
  public ImmutableParameterModel(String name,
                                 String description,
                                 MetadataType type,
                                 boolean hasDynamicType,
                                 boolean required,
                                 boolean isConfigOverride,
                                 boolean isComponentId,
                                 ExpressionSupport expressionSupport,
                                 Object defaultValue,
                                 ParameterRole role,
                                 ParameterDslConfiguration dslConfiguration,
                                 DisplayModel displayModel,
                                 LayoutModel layoutModel,
                                 ValueProviderModel valueProviderModel,
                                 List<StereotypeModel> allowedStereotypeModels,
                                 Set<ModelProperty> modelProperties,
                                 DeprecationModel deprecationModel,
                                 Set<String> semanticTerms,
                                 List<FieldValueProviderModel> fieldValueProviderModels) {
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
    this.isComponentId = isComponentId;
    this.valueProviderModel = valueProviderModel;
    this.allowedStereotypeModels = unmodifiableList(allowedStereotypeModels);
    this.deprecationModel = deprecationModel;
    this.semanticTerms = semanticTerms != null ? unmodifiableSet(semanticTerms) : emptySet();
    this.fieldValueProviderModels = unmodifiableList(fieldValueProviderModels);
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
  public List<StereotypeModel> getAllowedStereotypes() {
    return allowedStereotypeModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ValueProviderModel> getValueProviderModel() {
    return ofNullable(valueProviderModel);
  }

  @Override
  public List<FieldValueProviderModel> getFieldValueProviderModels() {
    if (fieldValueProviderModels == null) {
      fieldValueProviderModels = emptyList();
    }
    return fieldValueProviderModels;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isComponentId() {
    return this.isComponentId;
  }

  @Override
  public Optional<DeprecationModel> getDeprecationModel() {
    return ofNullable(deprecationModel);
  }

  @Override
  public boolean isDeprecated() {
    return deprecationModel != null;
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

  public String toString() {
    return getName() + " {" +
        "\n type=" + type +
        ",\n hasDynamicType=" + hasDynamicType +
        ",\n required=" + required +
        ",\n isConfigOverride=" + isConfigOverride +
        ",\n isComponentId=" + isComponentId +
        ",\n expressionSupport=" + expressionSupport +
        ",\n defaultValue=" + defaultValue +
        ",\n role=" + role +
        ",\n allowedStereotypeModels=" + allowedStereotypeModels +
        ",\n description='" + description + '\'' +
        ",\n semanticTerms ='" + semanticTerms + '\'' +
        "\n}";
  }
}

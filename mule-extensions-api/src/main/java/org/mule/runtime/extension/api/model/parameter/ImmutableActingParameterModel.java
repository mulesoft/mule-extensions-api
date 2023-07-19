/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.model.parameter;

import static org.mule.runtime.api.util.Preconditions.checkArgument;

import org.mule.runtime.api.meta.model.parameter.ActingParameterModel;

import java.util.Objects;

/**
 * Immutable implementation of {@link ActingParameterModel}
 *
 * @since 1.4.0
 */
public class ImmutableActingParameterModel implements ActingParameterModel {

  private final String name;
  private final boolean required;
  private String extractionExpression;

  /**
   * Creates a new instance with the given state
   *
   * @param name     the parameter's name. Cannot be blank.
   * @param required whether this parameter is required or not
   */
  public ImmutableActingParameterModel(String name, boolean required) {
    this(name, required, name);
  }

  /**
   * Creates a new instance with the given state
   *
   * @param name                 the parameter's name. Cannot be blank.
   * @param required             whether this parameter is required or not
   * @param extractionExpression the extraction expression to the acting parameter value.
   */
  public ImmutableActingParameterModel(String name, boolean required, String extractionExpression) {
    checkArgument(name != null && name.length() > 0, "name cannot be null or blank");
    this.name = name;
    this.required = required;
    this.extractionExpression = extractionExpression;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isRequired() {
    return required;
  }

  @Override
  public String getExtractionExpression() {
    if (extractionExpression == null) {
      extractionExpression = name;
    }
    return extractionExpression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ImmutableActingParameterModel)) {
      return false;
    }
    ImmutableActingParameterModel that = (ImmutableActingParameterModel) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(required, that.required) &&
        Objects.equals(extractionExpression, that.extractionExpression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, required, extractionExpression);
  }
}

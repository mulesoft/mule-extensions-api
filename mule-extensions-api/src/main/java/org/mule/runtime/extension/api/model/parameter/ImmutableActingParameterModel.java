/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.parameter;

import static org.mule.runtime.api.util.Preconditions.checkArgument;

import org.mule.runtime.api.meta.model.parameter.ActingParameterModel;

import java.util.Objects;

/**
 * Immutable implementation of {@link ActingParameterModel}
 *
 * @since 4.4
 */
public class ImmutableActingParameterModel implements ActingParameterModel {

  private final String name;
  private final boolean required;
  private final Object defaultValue;

  /**
   * Creates a new instance with the given state
   *
   * @param name          the parameter's name. Cannot be blank.
   * @param required      whether this parameter is required or not
   * @param defaultValue  this parameter's default value
   */
  public ImmutableActingParameterModel(String name, boolean required, Object defaultValue) {
    checkArgument(name != null && name.length() > 0, "name cannot be null or blank");
    this.name = name;
    this.required = required;
    this.defaultValue = defaultValue;
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
  public Object getDefaultValue() {
    return defaultValue;
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
        Objects.equals(defaultValue, that.defaultValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, required, defaultValue);
  }
}

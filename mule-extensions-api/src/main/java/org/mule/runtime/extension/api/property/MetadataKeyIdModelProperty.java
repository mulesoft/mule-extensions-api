/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import static java.util.Optional.ofNullable;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;

import java.util.Optional;

/**
 * A {@link ModelProperty} for a of {@link SourceModel} and {@link OperationModel} parameters that indicates that its a
 * {@link MetadataKeyId}.
 *
 * @since 1.0
 */
public final class MetadataKeyIdModelProperty implements ModelProperty {

  private final MetadataType type;
  private final String parameterName;
  private final String categoryName;
  public static final String NAME = "metadataKeyId";

  /**
   * Creates a new instance.
   *
   * @param type of the {@link ParameterModel} annotated with {@link MetadataKeyId}.
   */
  @Deprecated
  public MetadataKeyIdModelProperty(MetadataType type, String parameterName) {
    this(type, parameterName, null);
  }

  public MetadataKeyIdModelProperty(MetadataType type, String parameterName, String categoryName) {
    this.type = type;
    this.parameterName = parameterName;
    this.categoryName = categoryName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "MetadataKeyId";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  /**
   * @return the type of the {@link ParameterModel} annotated with {@link MetadataKeyId}
   */
  public MetadataType getType() {
    return type;
  }

  /**
   * @return the parameter name that is considered as the {@link MetadataKeyId}
   */
  public String getParameterName() {
    return parameterName;
  }

  /**
   * @return The category name where the current {@link MetadataKeyId} belongs.
   */
  public Optional<String> getCategoryName() {
    return ofNullable(categoryName);
  }
}

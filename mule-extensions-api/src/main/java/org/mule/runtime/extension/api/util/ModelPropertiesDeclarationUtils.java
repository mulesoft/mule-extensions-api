/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.internal.property.ComposedOperationModelProperty;
import org.mule.runtime.extension.internal.property.NoConnectivityErrorModelProperty;
import org.mule.runtime.extension.internal.property.NoErrorMappingModelProperty;
import org.mule.runtime.extension.internal.property.NoReconnectionStrategyModelProperty;
import org.mule.runtime.extension.internal.property.NoStreamingConfigurationModelProperty;
import org.mule.runtime.extension.internal.property.NoTransactionalActionModelProperty;
import org.mule.runtime.extension.internal.property.PagedOperationModelProperty;
import org.mule.runtime.extension.internal.property.TargetModelProperty;
import org.mule.runtime.extension.internal.property.TransactionalActionModelProperty;
import org.mule.runtime.extension.internal.property.TransactionalTypeModelProperty;

import java.util.Set;

/**
 * Provides a way to access the functionality on internal model properties when declaring crafted extensions.
 * 
 * @since 1.8
 */
public final class ModelPropertiesDeclarationUtils {

  private ModelPropertiesDeclarationUtils() {
    // nothing to do
  }

  public static final ModelProperty noStreamingConfigurationModelProperty() {
    return new NoStreamingConfigurationModelProperty();
  }

  public static final ModelProperty noTransactionalActionModelProperty() {
    return new NoTransactionalActionModelProperty();
  }

  public static final ModelProperty noReconnectionStrategyModelProperty() {
    return new NoReconnectionStrategyModelProperty();
  }

  public static final ModelProperty noConnectivityErrorModelProperty() {
    return new NoConnectivityErrorModelProperty();
  }

  public static final ModelProperty noErrorMappingModelProperty() {
    return new NoErrorMappingModelProperty();
  }

  public static final ModelProperty composedOperationModelProperty() {
    return new ComposedOperationModelProperty();
  }

  public static final ModelProperty targetModelProperty() {
    return new TargetModelProperty();
  }

  public static final boolean hasNoTransactionalActionModelProperty(EnrichableModel componentModel) {
    return componentModel.getModelProperty(NoTransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean hasErrorMappingModelProperty(EnrichableModel componentModel) {
    return componentModel.getModelProperty(NoErrorMappingModelProperty.class).isPresent();
  }

  public static final boolean hasPagedOperationModelProperty(EnrichableModel componentModel) {
    return componentModel.getModelProperty(PagedOperationModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalActionModelProperty(EnrichableModel componentModel) {
    return componentModel.getModelProperty(TransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalTypeModelProperty(EnrichableModel componentModel) {
    return componentModel.getModelProperty(TransactionalTypeModelProperty.class).isPresent();
  }

  public static boolean isTargetParameter(Set<ModelProperty> modelProperties) {
    return modelProperties.stream().anyMatch(modelProperty -> modelProperty instanceof TargetModelProperty);
  }

}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.privileged.util;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.HasModelProperties;
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
 * <p>
 * Being {@code privileged]}, this is not intended to be used outside of the scope of crafted extension declarations.
 * 
 * @since 1.8
 */
public final class ModelPropertiesDeclarationUtils {

  private ModelPropertiesDeclarationUtils() {
    // nothing to do
  }

  public static final void withNoStreamingConfiguration(HasModelProperties declarer) {
    declarer.withModelProperty(new NoStreamingConfigurationModelProperty());
  }

  public static final void withNoTransactionalAction(HasModelProperties declarer) {
    declarer.withModelProperty(new NoTransactionalActionModelProperty());
  }

  public static final void withNoReconnectionStrategy(HasModelProperties declarer) {
    declarer.withModelProperty(new NoReconnectionStrategyModelProperty());
  }

  public static final void withNoConnectivityError(HasModelProperties declarer) {
    declarer.withModelProperty(new NoConnectivityErrorModelProperty());
  }

  public static final void withNoErrorMapping(HasModelProperties declarer) {
    declarer.withModelProperty(new NoErrorMappingModelProperty());
  }

  public static final void pagedOperation(HasModelProperties declarer) {
    declarer.withModelProperty(new PagedOperationModelProperty());
  }

  public static final void targetModelProperty(HasModelProperties declarer) {
    declarer.withModelProperty(new TargetModelProperty());
  }

  public static final boolean isNoTransactionalAction(EnrichableModel componentModel) {
    return componentModel.getModelProperty(NoTransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean isNoErrorMapping(EnrichableModel componentModel) {
    return componentModel.getModelProperty(NoErrorMappingModelProperty.class).isPresent();
  }

  public static final boolean isPagedOperation(EnrichableModel componentModel) {
    return componentModel.getModelProperty(PagedOperationModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalAction(EnrichableModel componentModel) {
    return componentModel.getModelProperty(TransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalType(EnrichableModel componentModel) {
    return componentModel.getModelProperty(TransactionalTypeModelProperty.class).isPresent();
  }

  public static boolean isTargetParameter(Set<ModelProperty> modelProperties) {
    return modelProperties.stream().anyMatch(modelProperty -> modelProperty instanceof TargetModelProperty);
  }

}

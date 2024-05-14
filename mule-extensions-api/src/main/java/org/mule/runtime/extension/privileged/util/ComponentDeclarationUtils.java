/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.privileged.util;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclarer;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
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
 * Provides a way to access the functionality on internal model properties when declaring components on crafted extensions.
 * <p>
 * Being {@code privileged]}, this is not intended to be used outside of the scope of crafted extension declarations.
 * 
 * @since 1.8
 */
public final class ComponentDeclarationUtils {

  private ComponentDeclarationUtils() {
    // nothing to do
  }

  public static final void withNoStreamingConfiguration(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoStreamingConfigurationModelProperty());
  }

  public static final void withNoTransactionalAction(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoTransactionalActionModelProperty());
  }

  public static final void withNoReconnectionStrategy(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoReconnectionStrategyModelProperty());
  }

  // account for use cases where this is declared at the extension level for all its operations
  public static final void withNoReconnectionStrategy(ExtensionDeclarer declarer) {
    declarer.withModelProperty(new NoReconnectionStrategyModelProperty());
  }

  public static final void withNoConnectivityError(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoConnectivityErrorModelProperty());
  }

  public static final void withNoErrorMapping(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoErrorMappingModelProperty());
  }

  public static final void pagedOperation(ComponentDeclarer declarer) {
    declarer.withModelProperty(new PagedOperationModelProperty());
  }

  public static final void targetModelProperty(ParameterDeclarer declarer) {
    declarer.withModelProperty(new TargetModelProperty());
  }

  public static final boolean isNoTransactionalAction(ComponentModel componentModel) {
    return componentModel.getModelProperty(NoTransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean isNoErrorMapping(OperationModel componentModel) {
    return componentModel.getModelProperty(NoErrorMappingModelProperty.class).isPresent();
  }

  public static final boolean isPagedOperation(ComponentModel componentModel) {
    return componentModel.getModelProperty(PagedOperationModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalAction(ParameterModel paramModel) {
    return paramModel.getModelProperty(TransactionalActionModelProperty.class).isPresent();
  }

  public static final boolean hasTransactionalType(ParameterModel paramModel) {
    return paramModel.getModelProperty(TransactionalTypeModelProperty.class).isPresent();
  }

  public static boolean isTargetParameter(Set<ModelProperty> paramModelProperties) {
    return paramModelProperties.stream().anyMatch(modelProperty -> modelProperty instanceof TargetModelProperty);
  }

  public static boolean isTargetParameter(ParameterModel paramModel) {
    return isTargetParameter(paramModel.getModelProperties());
  }

}

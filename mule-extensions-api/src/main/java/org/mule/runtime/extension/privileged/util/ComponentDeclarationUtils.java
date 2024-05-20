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
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.tx.TransactionType;
import org.mule.runtime.extension.api.ExtensionConstants;
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

  /**
   * Marks that the operation being declared by the {@code declarer} may not have a configuring streaming strategy, regardless of
   * it being a streaming operation.
   * 
   * @param declarer the declarer of the operation to mark.
   */
  public static final void withNoStreamingConfiguration(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoStreamingConfigurationModelProperty());
  }

  /**
   * Marks that the operation being declared by the {@code declarer} may not have a configuring transactionalAction, regardless of
   * being transactional.
   * 
   * @param declarer the declarer of the operation to mark.
   */
  public static final void withNoTransactionalAction(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoTransactionalActionModelProperty());
  }

  /**
   * Marks that the operation being declared by the {@code declarer} may not have a
   * {@link ExtensionConstants#RECONNECTION_CONFIG_PARAMETER_NAME} parameter, regardless of being connected.
   * 
   * @param declarer the declarer of the operation to mark.
   */
  public static final void withNoReconnectionStrategy(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoReconnectionStrategyModelProperty());
  }

  /**
   * Marks that the operations of the extension being declared by the {@code declarer} may not have a
   * {@link ExtensionConstants#RECONNECTION_CONFIG_PARAMETER_NAME} parameter, regardless of them being connected.
   * 
   * @param declarer the declarer of the extension to mark.
   */
  // account for use cases where this is declared at the extension level for all its operations
  public static final void withNoReconnectionStrategy(ExtensionDeclarer declarer) {
    declarer.withModelProperty(new NoReconnectionStrategyModelProperty());
  }

  /**
   * Marks that the operation being declared by the {@code declarer} will avoid propagating connectivity errors from operations,
   * regardless of being connected.
   * 
   * @param declarer the declarer of the operation to mark.
   */
  public static final void withNoConnectivityError(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoConnectivityErrorModelProperty());
  }

  /**
   * Marks that the operation being declared by the {@code declarer} may not contain errorMappings.
   * 
   * @param declarer the declarer of the operation to mark.
   */
  public static final void withNoErrorMapping(OperationDeclarer declarer) {
    declarer.withModelProperty(new NoErrorMappingModelProperty());
  }

  /**
   * Marks that the component being declared by the {@code declarer} is paged.
   * 
   * @param declarer the declarer of the component to mark.
   */
  public static final void asPagedOperation(ComponentDeclarer declarer) {
    declarer.withModelProperty(new PagedOperationModelProperty());
  }

  /**
   * @param componentModel the component to check for.
   * @return whether {@code componentModel} may not have a configuring transactionalAction.
   */
  public static final boolean isNoTransactionalAction(ComponentModel componentModel) {
    return componentModel.getModelProperty(NoTransactionalActionModelProperty.class).isPresent();
  }

  /**
   * @param componentModel the operation to check for.
   * @return whether {@code componentModel} may not contain errorMappings.
   */
  public static final boolean isNoErrorMapping(OperationModel componentModel) {
    return componentModel.getModelProperty(NoErrorMappingModelProperty.class).isPresent();
  }

  /**
   * @param componentModel the component to check for.
   * @return whether {@code componentModel} is paged.
   */
  public static final boolean isPagedOperation(ComponentModel componentModel) {
    return componentModel.getModelProperty(PagedOperationModelProperty.class).isPresent();
  }

  /**
   * @param paramModel the parameter to check for.
   * @return whether {@code paramModel} is the parameter considered to inject the value of the Transactional Action.
   */
  public static final boolean isTransactionalAction(ParameterModel paramModel) {
    return paramModel.getModelProperty(TransactionalActionModelProperty.class).isPresent();
  }

  /**
   * @param paramModel the parameter to check for.
   * @return whether {@code paramModel} is the parameter is a {@link TransactionType}.
   */
  public static final boolean isTransactionalType(ParameterModel paramModel) {
    return paramModel.getModelProperty(TransactionalTypeModelProperty.class).isPresent();
  }

  /**
   * @param paramModelProperties the parameter to check for.
   * @return whether the paramModel owning the provided {@code paramModelProperties} is the parameter considered as a Target type.
   */
  public static boolean isTargetParameter(Set<ModelProperty> paramModelProperties) {
    return paramModelProperties.stream().anyMatch(modelProperty -> modelProperty instanceof TargetModelProperty);
  }

  /**
   * @param paramModel the parameter to check for.
   * @return whether {@code paramModel} is the parameter considered as a Target type.
   */
  public static boolean isTargetParameter(ParameterModel paramModel) {
    return isTargetParameter(paramModel.getModelProperties());
  }

}

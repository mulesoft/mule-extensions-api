/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import org.mule.runtime.api.meta.model.ConnectableComponentModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.BaseDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.internal.property.NoConnectivityErrorModelProperty;
import org.mule.runtime.extension.internal.property.NoReconnectionStrategyModelProperty;

/**
 * Utility methods for analyzing connectivity aspects of {@link ExtensionModel} instances.
 *
 * @since 1.5
 */
public class ExtensionConnectivityUtils {

  private ExtensionConnectivityUtils() {}

  /**
   * @param componentModel a {@link ConnectableComponentModel}
   * @return Whether a component modeled by the given {@code componentModel} would need a connection to be provided in order to
   *         function.
   */
  public static boolean isConnectionProvisioningRequired(ExtensionModel extensionModel,
                                                         ConnectableComponentModel componentModel) {
    return componentModel.requiresConnection()
        && isConnectivityErrorSupported(componentModel);
  }

  /**
   * @param componentModel an {@link ConnectableComponentModel}
   * @return Whether the component modeled by the given {@code componentModel} supports having a reconnection strategy.
   */
  public static boolean isReconnectionStrategySupported(ConnectableComponentModel componentModel) {
    return !componentModel.getModelProperty(NoReconnectionStrategyModelProperty.class).isPresent();
  }

  /**
   * @param declaration an {@link ExtensionDeclaration}.
   * @return Whether the components belonging to the extension declared by the given {@code declaration} support having a
   *         reconnection strategy.
   */
  public static boolean isReconnectionStrategySupported(ExtensionDeclaration declaration) {
    return isReconnectionStrategySupported((BaseDeclaration<?>) declaration);
  }

  /**
   * @param declaration an {@link ExecutableComponentDeclaration}.
   * @return Whether the component declared by the given {@code declaration} supports having a reconnection strategy.
   */
  public static boolean isReconnectionStrategySupported(ExecutableComponentDeclaration<?> declaration) {
    return isReconnectionStrategySupported((BaseDeclaration<?>) declaration);
  }

  private static boolean isReconnectionStrategySupported(BaseDeclaration<?> declaration) {
    return !declaration.getModelProperty(NoReconnectionStrategyModelProperty.class).isPresent();
  }

  /**
   * @param declaration an {@link ExecutableComponentDeclaration}.
   * @return Whether the component declared by the given {@code declaration} supports having a reconnection strategy.
   */
  public static boolean isConnectivityErrorSupported(ExecutableComponentDeclaration<?> declaration) {
    return isConnectivityErrorSupported((BaseDeclaration<?>) declaration);
  }

  private static boolean isConnectivityErrorSupported(BaseDeclaration<?> declaration) {
    return !declaration.getModelProperty(NoConnectivityErrorModelProperty.class).isPresent();
  }

  private static boolean isConnectivityErrorSupported(ConnectableComponentModel componentModel) {
    return !componentModel.getModelProperty(NoConnectivityErrorModelProperty.class).isPresent();
  }
}

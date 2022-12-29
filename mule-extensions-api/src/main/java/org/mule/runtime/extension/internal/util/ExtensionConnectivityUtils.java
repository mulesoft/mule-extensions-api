/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import org.mule.runtime.api.meta.model.ConnectableComponentModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.internal.property.NoConnectionProvisioningModelProperty;

/**
 * Utility methods for analyzing connectivity aspects of {@link ExtensionModel} instances.
 *
 * @since 1.6
 */
public class ExtensionConnectivityUtils {

  private ExtensionConnectivityUtils() {}

  /**
   * @param componentModel a {@link ConnectableComponentModel}
   * @return Whether a component modeled by the given {@code componentModel} would need a connection to be provided in order to
   *         function.
   */
  public static boolean requiresConnectionProvisioning(ConnectableComponentModel componentModel) {
    return componentModel.requiresConnection()
        && !componentModel.getModelProperty(NoConnectionProvisioningModelProperty.class).isPresent();
  }

  /**
   * @param operationDeclaration an {@link OperationDeclaration}
   * @return Whether an operation declared with the given {@code operationDeclaration} would need a connection to be provided in
   *         order to function.
   */
  public static boolean requiresConnectionProvisioning(OperationDeclaration operationDeclaration) {
    return operationDeclaration.isRequiresConnection()
        && !operationDeclaration.getModelProperty(NoConnectionProvisioningModelProperty.class).isPresent();
  }
}

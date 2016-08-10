/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.connection;


import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.Named;
import org.mule.runtime.extension.api.introspection.parameter.ParameterizedModel;

/**
 * Introspection model for {@link ConnectionProvider} types.
 * <p>
 * Provider models implement the flyweight pattern. This means
 * that a given operation should only be represented by only
 * one instance of this class. Thus, if the same operation is
 * contained by different {@link HasConnectionProviderModels} instances,
 * then each of those containers should reference the same
 * operation model instance.
 *
 * @since 1.0
 */
public interface ConnectionProviderModel extends Named, Described, EnrichableModel, ParameterizedModel {

  /**
   * @return the type of connection management that the provider performs
   */
  ConnectionManagementType getConnectionManagementType();
}

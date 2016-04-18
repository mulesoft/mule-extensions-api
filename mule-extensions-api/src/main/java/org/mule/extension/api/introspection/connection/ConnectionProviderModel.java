/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.connection;


import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.extension.api.introspection.Described;
import org.mule.extension.api.introspection.EnrichableModel;
import org.mule.extension.api.introspection.parameter.ParametrizedModel;

/**
 * Introspection model for {@link ConnectionProvider} types.
 *
 * @param <Config>     the generic type for the configuration objects that the returned {@link ConnectionProvider providers} accept
 * @param <Connection> the generic type for the connections that the returned  {@link ConnectionProvider providers} produce
 * @since 1.0
 */
public interface ConnectionProviderModel<Config, Connection> extends Described, EnrichableModel, ParametrizedModel
{

}

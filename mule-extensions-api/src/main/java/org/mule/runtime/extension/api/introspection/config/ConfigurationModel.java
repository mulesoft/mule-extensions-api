/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.config;

import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.operation.HasOperationModels;
import org.mule.runtime.extension.api.introspection.source.HasSourceModels;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParametrizedModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.connection.HasConnectionProviderModels;

/**
 * A named configuration for an extension
 * <p/>
 * Configurations describe different ways to initialize a scope for operations.
 * Upon execution, each operation will be associated to a given configuration, so configurations define both
 * a set of shared properties used in operations, and a common context to relate operations.
 * <p/>
 * The configuration can also imply different implicit behaviors not strictly attached to the operations
 * <p/>
 * The configuration is also the place in which cross operation, extension level attributes are configured.
 * Every {@link ExtensionModel} is required to have at least one {@link ConfigurationModel}.
 * That {@link ConfigurationModel} is defined as the &quot;default configuration&quot;
 * <p>
 * Although the {@link SourceModel}s, {@link OperationModel}s and {@link ConnectionProviderModel}s
 * defined at the extension level are available to every single config, configs can also
 * define its own set of those which are exclusive to them.
 *
 * @since 1.0
 */
public interface ConfigurationModel extends Described, EnrichableModel, ParametrizedModel,
        HasOperationModels, HasSourceModels, HasConnectionProviderModels
{

}

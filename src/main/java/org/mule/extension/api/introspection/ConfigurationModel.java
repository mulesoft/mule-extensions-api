/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

/**
 * A named configuration for an extension
 * <p/>
 * Configurations describe different ways to initialize a scope for operations.
 * Upon execution, each operation will be associated to a given configuration, so configurations define both
 * a set of shared properties used in operations, and a common context to relate operations.
 * <p/>
 * The configuration can also imply different implicit behaviors not strictly attached to the operations
 * (e.g.: A connector supporting both stateful connections and OAuth2 authentication. Depending on the
 * configuration used, the same connector will have different reconnection strategies).
 * <p/>
 * The configuration is also the place in which cross operation, extension level attributes are configured.
 * Every {@link ExtensionModel} is required to have at least one {@link ConfigurationModel}.
 * That {@link ConfigurationModel} is defined as the &quot;default configuration&quot;
 *
 * @since 1.0
 */
public interface ConfigurationModel extends Described, EnrichableModel, InterceptableModel, ParametrizedModel
{

    /**
     * @return the {@link ExtensionModel} that owns {@code this} model
     */
    ExtensionModel getExtensionModel();

    /**
     * Returns the {@link ConfigurationFactory} for this instance
     */
    ConfigurationFactory getConfigurationFactory();

}

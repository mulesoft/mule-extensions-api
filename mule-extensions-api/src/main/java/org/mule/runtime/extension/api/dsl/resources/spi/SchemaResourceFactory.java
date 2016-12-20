/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.resources.spi;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.resources.GeneratedResource;

/**
 * Creates a {@link GeneratedResource} instance with the schema from an {@link ExtensionModel} instance.
 * <p/>
 * It's a marker interface for doing a look up through SPI in the org.mule.runtime.config.spring.ModuleDelegatingEntityResolver
 * class, where given an ExtensionModel it returns a resource with the schema in it's #getContents() method.
 * <p/>
 * Implementations are to be reusable and thread-safe.
 * TODO MULE-9865: when implemented, the only interface to execute SPI will be the DslResourceFactory and this interface should be removed (schemas will be generated dynamically)
 *
 * @since 1.0
 */
public interface SchemaResourceFactory extends DslResourceFactory {

}

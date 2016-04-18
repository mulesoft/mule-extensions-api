/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.spi;


import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.DescribingContext;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclarer;

/**
 * Every Extension must ship with one implementation of this interface
 * which has to be discoverable through standard SPI and have a default constructor.
 * <p/>
 * This describer returns a {@link ExtensionDeclarer} which contains a raw representation of the
 * extension.
 * <p/>
 * The platform will discover all the describers, transform the returned {@link ExtensionDeclarer}s
 * into {@link ExtensionModel extensionModels} and register them.
 * <p/>
 * To allow customization of the describing process, implementations will use standard SPI discovery mechanism
 * to locale registered instances of {@link ModelEnricher}
 * which are invoked in order.
 *
 * @since 1.0
 */
public interface Describer
{

    /**
     * Describes the extension as a {@link ExtensionDeclarer}
     *
     * @param context a {@link DescribingContext} with state relevant to the operation
     * @return a {@link ExtensionDeclarer}
     */
    ExtensionDeclarer describe(DescribingContext context);

}

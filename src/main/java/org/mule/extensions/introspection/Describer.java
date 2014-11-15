/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;


import org.mule.extensions.introspection.declaration.Construct;
import org.mule.extensions.introspection.spi.DescriberPostProcessor;

/**
 * Every Extension must ship with one implementation of this interface
 * which has to be discoverable through standard SPI and have a default constructor.
 * <p/>
 * This describer returns a {@link Construct} which contains a raw representation of the
 * extension.
 * <p/>
 * The platform will discover all the describers, transform the returned {@link Construct}s
 * into {@link Extension}s and register them.
 * <p/>
 * To allow customization of the describing process, implementations will use standard SPI discovery mechanism
 * to locale registered instances of {@link DescriberPostProcessor}
 * which are invoked in order.
 *
 * @since 1.0
 */
public interface Describer
{

    /**
     * Describes the extension as a {@link Construct}
     *
     * @return a {@link Construct}
     */
    Construct describe();

}

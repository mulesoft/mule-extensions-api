/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.fluent;

import org.mule.extension.api.introspection.ExceptionEnricherFactory;

import java.util.Optional;

/**
 * A contract interface for an object capable of adding an {@link ExceptionEnricherFactory}
 * into a {@link Descriptor}
 *
 * @param <D> the generic type of the target {@link Descriptor}
 * @since 1.0
 */
public interface HasExceptionEnricher<D extends Descriptor>
{

    /**
     * Provides the given {@link ExceptionEnricherFactory}
     *
     * @param enricherFactory a concrete ExceptionEnricherFactory
     * @return the configured {@link Descriptor}
     */
    D withExceptionEnricherFactory(Optional<ExceptionEnricherFactory> enricherFactory);

}

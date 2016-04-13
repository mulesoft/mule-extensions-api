/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.exception;

import org.mule.extension.api.introspection.Interceptable;

import java.util.Optional;

/**
 * Contract for models capable of providing an optional {@link ExceptionEnricherFactory}
 *
 * @see Interceptable
 * @since 1.0
 */
public interface ExceptionEnrichableModel
{

    /**
     * @return an {@link Optional} concrete {@link ExceptionEnricherFactory}
     */
    Optional<ExceptionEnricherFactory> getExceptionEnricherFactory();
}

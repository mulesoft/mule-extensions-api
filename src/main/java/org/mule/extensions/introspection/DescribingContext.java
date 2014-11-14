/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;

import org.mule.extensions.introspection.declaration.DeclarationConstruct;
import org.mule.extensions.introspection.spi.DescriberPostProcessor;

import java.util.Map;

/**
 * Used for propagating state across all the components
 * that may take part on the activation of an {@link Extension}
 * and its {@link DescriberPostProcessor}s
 * <p/>
 * Once the {@link Describer} finishes applying its logic,
 * it will propagate this context through all the found
 * {@link DescriberPostProcessor}s, which means that any
 * side effects applied by any of the before mentioned will be visible by the next ones.
 *
 * @since 1.0.0
 */
public interface DescribingContext
{

    /**
     * The {@link ExtensionBuilder} in which
     * the extension is being described into
     *
     * @return a non {@code null} {@link ExtensionBuilder}
     */
    DeclarationConstruct getDeclarationConstruct();

    /**
     * A {@link Map} to hold custom parameters that implementations of
     * {@link Describer} and {@link DescriberPostProcessor} might
     * want to share with each other
     *
     * @return a non {@code null} map. Not be assumed thread-safe
     */
    Map<String, Object> getCustomParameters();
}

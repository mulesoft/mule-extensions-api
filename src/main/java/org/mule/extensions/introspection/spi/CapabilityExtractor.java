/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.spi;

import org.mule.extensions.introspection.Capable;
import org.mule.extensions.introspection.Extension;
import org.mule.extensions.introspection.declaration.Construct;
import org.mule.extensions.introspection.declaration.DeclarationConstruct;
import org.mule.extensions.introspection.declaration.HasCapabilities;

import java.util.ServiceLoader;

/**
 * A component capable of extracting one specific capability
 * out of a {@link Class} that composes a {@link Capable}
 * <p/>
 * Because actual capabilities might be defined across several modules (or even extensions!)
 * the actual extractors are fetched through SPI, using the standard {@link ServiceLoader}
 * contract.
 * <p/>
 * Each implementation of this class has to aim to one and only one specific capability type. It's
 * this extractor's responsibility to ignore the capabilities which are outside of its domain
 * and to ignore any extension types which don't support the given capability
 *
 * @since 1.0.0
 */
public interface CapabilityExtractor
{

    /**
     * Looks for a specific capability in the given {@code extensionType}.
     * and declares it into the given {@code builder}. After doing such,
     * the found capability is returned (or {@code null} if not found)
     *
     * @param declaration   a {@link DeclarationConstruct} describing the {@link Extension} to be built
     * @param capableType the type of the {@link Capable} class
     * @param capableCallback  a {@link HasCapabilities} on which the {@link Capable} is to be registered.
     * @return the extract capability
     */
    public Object extractCapability(DeclarationConstruct declaration, Class<?> capableType, HasCapabilities<? extends Construct> capableCallback);
}

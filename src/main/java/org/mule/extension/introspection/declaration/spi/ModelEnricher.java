/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.spi;

import org.mule.extension.introspection.EnrichableModel;
import org.mule.extension.introspection.declaration.DescribingContext;
import org.mule.extension.introspection.declaration.fluent.Descriptor;


/**
 * A component which can enrich {@link Descriptor descriptors} before they are actually turned
 * into models. This is useful for implementations of the extensions API which rely
 * on non programmatic describers.
 * <p/>
 * Because actual capabilities might be defined across several modules (or even extensions!)
 * instances are fetched through SPI
 *
 * @since 4.0
 */
public interface ModelEnricher
{

    /**
     * Enriches the descriptor provided in the given {@code describingContext}. If this
     * enricher requires receiving state or wants to pass information to the next enricher,
     * then it should do it through the given {@code describingContext}
     *
     * @param describingContext the {@link DescribingContext} on which a {@link EnrichableModel} is being described
     */
    void enrich(DescribingContext describingContext);

}

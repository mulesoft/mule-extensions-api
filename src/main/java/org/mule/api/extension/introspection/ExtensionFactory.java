/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection;

import org.mule.api.extension.introspection.declaration.DescribingContext;
import org.mule.api.extension.introspection.declaration.fluent.Descriptor;
import org.mule.api.extension.introspection.declaration.spi.ModelEnricher;

/**
 * A factory that can take a {@link Descriptor} and transform it into an actual
 * {@link ExtensionModel}.
 * <p/>
 * This factory is also responsible for leveraging the available {@link ModelEnricher} implementations
 * to enrich the model before it's actually transformed into a {@link EnrichableModel}
 *
 * @since 1.0
 */
public interface ExtensionFactory
{

    /**
     * Creates a {@link ExtensionModel} from the given {@link Descriptor}
     *
     * @param descriptor a {@link Descriptor}. Cannot be {@code null}
     * @return an {@link ExtensionModel}
     */
    ExtensionModel createFrom(Descriptor descriptor);

    /**
     * Creates a {@link ExtensionModel} from the given {@link Descriptor}
     * using a specifying {@code describingContext}
     *
     * @param descriptor        a {@link Descriptor}. Cannot be {@code null}
     * @param describingContext a {@link DescribingContext}, useful to specify custom settings
     * @return an {@link ExtensionModel}
     */
    ExtensionModel createFrom(Descriptor descriptor, DescribingContext describingContext);
}

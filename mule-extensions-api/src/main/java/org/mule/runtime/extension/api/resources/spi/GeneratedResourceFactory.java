/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.resources.spi;

import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.resources.GeneratedResource;

import java.util.Optional;

/**
 * Factory class which creates a {@link GeneratedResource} instance
 * from an {@link ExtensionModel} instance.
 * <p/>
 * Implementations are to be reusable and thread-safe.
 *
 * @since 1.0
 */
public interface GeneratedResourceFactory
{

    /**
     * Creates a new {@link GeneratedResource} from the given {@code extensionModel}.
     * <p>
     * Because not every implementation of this class necessarily applies to
     * every {@link ExtensionModel}, this method returns an {@link Optional}
     * which will be empty if the kind of generated resource is not valid
     * for the given {@code extensionModel}
     *
     * @param extensionModel the {@link ExtensionModel} that requires the resource
     * @return an {@link Optional} {@link GeneratedResource}
     */
    Optional<GeneratedResource> generateResource(ExtensionModel extensionModel);
}

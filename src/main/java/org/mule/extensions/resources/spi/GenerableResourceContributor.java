/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.resources.spi;

import org.mule.extensions.introspection.Extension;
import org.mule.extensions.resources.ResourcesGenerator;

/**
 * A delegate object that is obtained through standard SPI discovery mechanism
 * and that acts as a delegate inside a chain of responsibility pattern, in order to
 * generate backing resources for all the {@link Extension}s
 * declared between a module
 * <p/>
 * Each contributor is responsible for declaring the resources that it wants to generate/contribute to
 * and add its piece of content.
 * <p/>
 * There might be situations in which a contributor should not generate anything for a given
 * {@link Extension}. For example, a contributor that
 * writes a XML schema will pass on a extension which doesn't have xml capabilities. It's completely
 * up to each contributor to decide when and when not contribute to a extension
 *
 * @since 1.0
 */
public interface GenerableResourceContributor
{

    /**
     * Contributes if necessary to the generation of the given {@code extension}'s resources.
     *
     * @param extension          the {@link Extension} that requires the resource
     * @param resourcesGenerator a {@link ResourcesGenerator} used to obtain the resources
     */
    void contribute(Extension extension, ResourcesGenerator resourcesGenerator);
}

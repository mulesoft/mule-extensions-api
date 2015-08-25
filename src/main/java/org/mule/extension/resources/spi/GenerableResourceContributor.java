/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.resources.spi;

import org.mule.extension.introspection.ExtensionModel;
import org.mule.extension.resources.ResourcesGenerator;

/**
 * A delegate object that is obtained through standard SPI discovery mechanism
 * and that acts as a delegate inside a chain of responsibility pattern, in order to
 * generate backing resources for all the {@link ExtensionModel extensionModels}
 * declared between a module
 * <p/>
 * Each contributor is responsible for declaring the resources that it wants to generate/contribute to
 * and add its piece of content.
 * <p/>
 * There might be situations in which a contributor should not generate anything for a given
 * {@link ExtensionModel}. For example, a contributor that
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
     * @param extensionModel          the {@link ExtensionModel} that requires the resource
     * @param resourcesGenerator a {@link ResourcesGenerator} used to obtain the resources
     */
    void contribute(ExtensionModel extensionModel, ResourcesGenerator resourcesGenerator);
}

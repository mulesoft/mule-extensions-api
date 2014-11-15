/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.resources;

import org.mule.extensions.introspection.Extension;

/**
 * A resource that supports a
 * {@link Extension} and which can be generated
 * by the runtime
 *
 * @since 1.0
 */
public interface GenerableResource
{

    /**
     * The path in which the file will be found after being generated.
     * Also serves as the resource's primary key
     *
     * @return a {@link java.lang.String} not empty nor {@code null}
     */
    String getFilePath();

    /**
     * The builder which {@link org.mule.extensions.resources.spi.GenerableResourceContributor}s
     * will use to contribute their part
     *
     * @return a non {@code null} {@link java.lang.StringBuilder}
     */
    StringBuilder getContentBuilder();
}

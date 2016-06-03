/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

public interface HasConnectionProviderDeclarer
{

    /**
     * Adds a connection provider of the given {@code name}
     *
     * @param name a non blank name
     * @return a {@link ConnectionProviderDeclarer} which allows describing the created provider
     */
    ConnectionProviderDeclarer withConnectionProvider(String name);

    void withConnectionProvider(ConnectionProviderDeclarer declarer);
}

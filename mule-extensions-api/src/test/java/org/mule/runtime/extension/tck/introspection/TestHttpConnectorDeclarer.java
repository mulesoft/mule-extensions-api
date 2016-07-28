/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.tck.introspection;

import static org.mockito.Mockito.mock;
import static org.mule.runtime.extension.api.Category.COMMUNITY;
import static org.mule.runtime.extension.api.introspection.connection.ConnectionManagementType.NONE;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.extension.api.introspection.config.ConfigurationFactory;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderFactory;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ComponentDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.io.InputStream;
import java.io.Serializable;


/**
 * A simple pojo containing reference information for making test around a {@link ExtensionDeclarer}
 * which represents a theoretical &quot;Http connector&quot; extension.
 * <p>
 * It contains an actual {@link ExtensionDeclarer} that can be accessed through the {@link #getExtensionDeclarer()}
 * method plus some other getters which provides access to other declaration components
 * that you might want to make tests against.
 * <p>
 * This case focuses on the scenario in which each config has its own set of operations, providers and sources.
 *
 * @since 1.0
 */
public class TestHttpConnectorDeclarer
{

    public static final Class<?> REQUESTER_CONNECTION_PROVIDER_CONNECTION_TYPE = Integer.class;
    public static final String EXTENSION_NAME = "http";
    public static final String EXTENSION_DESCRIPTION = "Http Connector";
    public static final String VENDOR = "Mulesoft";
    public static final String REQUESTER_CONFIG_NAME = "requester";
    public static final String REQUESTER_CONFIG_DESCRIPTION = "http requester";
    public static final String REQUEST_OPERATION_NAME = "request";
    public static final String PATH = "path";
    public static final String REQUESTER_PROVIDER = "requesterProvider";
    public static final String LISTENER_CONFIG_NAME = "listener";
    public static final String LISTENER_CONFIG_DESCRIPTION = "http listener";
    public static final String LISTEN_MESSAGE_SOURCE = "listen";
    public static final String PORT = "port";
    public static final int DEFAULT_PORT = 8080;
    public static final String VERSION = "1.0";
    public static final String STATIC_RESOURCE_OPERATION_NAME = "staticResource";
    public static final MuleVersion MIN_MULE_VERSION = new MuleVersion("4.0");

    private final ExtensionDeclarer extensionDeclarer = new ExtensionDeclarer();
    private final ConnectionProviderFactory requesterConnectionProviderFactory = mock(ConnectionProviderFactory.class);
    private final ConfigurationFactory configurationFactory = mock(ConfigurationFactory.class);
    private final Source<Object, Attributes> source = mock(Source.class);
    private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

    public TestHttpConnectorDeclarer()
    {
        extensionDeclarer.named(EXTENSION_NAME).describedAs(EXTENSION_DESCRIPTION).fromVendor(VENDOR).onVersion(VERSION).withCategory(COMMUNITY).withMinMuleVersion(MIN_MULE_VERSION);
        ComponentDeclarer staticResource = extensionDeclarer.withOperation(STATIC_RESOURCE_OPERATION_NAME);
        staticResource.withOutput().ofType(typeLoader.load(InputStream.class));
        staticResource.withRequiredParameter(PATH).ofType(typeLoader.load(String.class));

        ConfigurationDeclarer requesterConfig = extensionDeclarer.withConfig(REQUESTER_CONFIG_NAME).describedAs(REQUESTER_CONFIG_DESCRIPTION).createdWith(configurationFactory);
        ComponentDeclarer request = requesterConfig.withOperation(REQUEST_OPERATION_NAME);
        request.withOutput().ofType(typeLoader.load(InputStream.class));
        request.withRequiredParameter(PATH).ofType(typeLoader.load(String.class));

        requesterConfig.withConnectionProvider(REQUESTER_PROVIDER)
                .createdWith(requesterConnectionProviderFactory)
                .whichGivesConnectionsOfType(REQUESTER_CONNECTION_PROVIDER_CONNECTION_TYPE)
                .withConnectionManagementType(NONE);

        ConfigurationDeclarer listenerRequester = extensionDeclarer.withConfig(LISTENER_CONFIG_NAME).describedAs(LISTENER_CONFIG_DESCRIPTION).createdWith(configurationFactory);
        ComponentDeclarer listen = listenerRequester.withMessageSource(LISTEN_MESSAGE_SOURCE).sourceCreatedBy(() -> source);
        listen.withOutput().ofType(typeLoader.load(InputStream.class));
        listen.withOutputAttributes().ofType(typeLoader.load(Serializable.class));
        listen.withOptionalParameter(PORT).ofType(typeLoader.load(Integer.class)).defaultingTo(DEFAULT_PORT);
    }

    public ExtensionDeclarer getExtensionDeclarer()
    {
        return extensionDeclarer;
    }

    public ConnectionProviderFactory getRequesterConnectionProviderFactory()
    {
        return requesterConnectionProviderFactory;
    }

    public Source<Object, Attributes> getSource()
    {
        return source;
    }
}

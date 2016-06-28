/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.Category.COMMUNITY;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.EXTENSION_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.EXTENSION_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.LISTENER_CONFIG_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.LISTENER_CONFIG_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.LISTEN_MESSAGE_SOURCE;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.MIN_MULE_VERSION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.PATH;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.PORT;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.REQUESTER_CONFIG_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.REQUESTER_CONFIG_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.REQUESTER_CONNECTION_PROVIDER_CONNECTION_TYPE;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.REQUESTER_PROVIDER;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.REQUEST_OPERATION_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.STATIC_RESOURCE_OPERATION_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.VENDOR;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.VERSION;

import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer;

import java.io.InputStream;
import java.io.Serializable;

import org.junit.Test;

public class ComplexExtensionDeclarationTestCase extends BaseDeclarationTestCase
{

    private TestHttpConnectorDeclarer testDeclarer = new TestHttpConnectorDeclarer();
    private ExtensionDeclaration extensionDeclaration = testDeclarer.getExtensionDeclarer().getDeclaration();


    @Test
    public void assertDeclaration()
    {
        assertThat(extensionDeclaration.getName(), is(EXTENSION_NAME));
        assertThat(extensionDeclaration.getDescription(), is(EXTENSION_DESCRIPTION));
        assertThat(extensionDeclaration.getVersion(), is(VERSION));
        assertThat(extensionDeclaration.getConfigurations(), hasSize(2));
        assertThat(extensionDeclaration.getVendor(), is(VENDOR));
        assertThat(extensionDeclaration.getMinMuleVersion(), is(MIN_MULE_VERSION));
        assertThat(extensionDeclaration.getCategory(), is(COMMUNITY));
        assertThat(extensionDeclaration.getOperations(), hasSize(1));
        assertThat(extensionDeclaration.getConnectionProviders(), is(empty()));
        assertThat(extensionDeclaration.getMessageSources(), is(empty()));
    }

    @Test
    public void listenerConfig()
    {
        ConfigurationDeclaration listener = extensionDeclaration.getConfigurations().get(1);
        assertThat(listener.getName(), is(LISTENER_CONFIG_NAME));
        assertThat(listener.getDescription(), is(LISTENER_CONFIG_DESCRIPTION));
        assertThat(listener.getOperations(), is(empty()));
        assertThat(listener.getConnectionProviders(), is(empty()));
        assertThat(listener.getMessageSources(), hasSize(1));
    }

    @Test
    public void listenerSource()
    {
        SourceDeclaration source = extensionDeclaration.getConfigurations().get(1).getMessageSources().get(0);
        assertThat(source.getName(), is(LISTEN_MESSAGE_SOURCE));
        assertThat(source.getSourceFactory().createSource(), is(sameInstance(testDeclarer.getSource())));
        assertDataType(source.getOutputPayload().getType(), InputStream.class, BinaryType.class);
        assertDataType(source.getOutputAttributes().getType(), Serializable.class, ObjectType.class);
        assertThat(source.getParameters(), hasSize(1));

        ParameterDeclaration parameter = source.getParameters().get(0);
        assertThat(parameter.getName(), is(PORT));
        assertThat(parameter.isRequired(), is(false));
        assertDataType(parameter.getType(), Integer.class, NumberType.class);
    }

    @Test
    public void requesterConfig()
    {
        ConfigurationDeclaration listener = extensionDeclaration.getConfigurations().get(0);
        assertThat(listener.getName(), is(REQUESTER_CONFIG_NAME));
        assertThat(listener.getDescription(), is(REQUESTER_CONFIG_DESCRIPTION));
        assertThat(listener.getOperations(), hasSize(1));
        assertThat(listener.getConnectionProviders(), hasSize(1));
        assertThat(listener.getMessageSources(), is(empty()));
    }

    @Test
    public void requestOperation()
    {
        OperationDeclaration operation = extensionDeclaration.getConfigurations().get(0).getOperations().get(0);
        assertThat(operation.getName(), is(REQUEST_OPERATION_NAME));
        assertDataType(operation.getOutputPayload().getType(), InputStream.class, BinaryType.class);
        assertThat(operation.getParameters(), hasSize(1));

        ParameterDeclaration parameter = operation.getParameters().get(0);
        assertThat(parameter.getName(), is(PATH));
        assertDataType(parameter.getType(), String.class, StringType.class);
    }

    @Test
    public void staticResourceOperation()
    {
        OperationDeclaration operation = extensionDeclaration.getOperations().get(0);
        assertThat(operation.getName(), is(STATIC_RESOURCE_OPERATION_NAME));
        assertDataType(operation.getOutputPayload().getType(), InputStream.class, BinaryType.class);

        assertThat(operation.getParameters(), hasSize(1));
        ParameterDeclaration parameter = operation.getParameters().get(0);
        assertThat(parameter.getName(), is(PATH));
        assertDataType(parameter.getType(), String.class, StringType.class);
    }

    @Test
    public void connectionProvider()
    {
        ConnectionProviderDeclaration provider = extensionDeclaration.getConfigurations().get(0).getConnectionProviders().get(0);
        assertThat(provider.getName(), is(REQUESTER_PROVIDER));
        assertThat(provider.getFactory(), is(sameInstance(testDeclarer.getRequesterConnectionProviderFactory())));
        assertThat(provider.getConnectionType(), equalTo(REQUESTER_CONNECTION_PROVIDER_CONNECTION_TYPE));
    }
}

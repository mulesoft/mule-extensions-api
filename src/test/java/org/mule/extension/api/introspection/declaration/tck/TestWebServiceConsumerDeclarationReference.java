/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration.tck;

import static org.mockito.Mockito.mock;
import static org.mule.extension.api.introspection.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.extension.api.introspection.ExpressionSupport.REQUIRED;
import org.mule.extension.api.introspection.ConfigurationFactory;
import org.mule.extension.api.introspection.ConnectionProviderFactory;
import org.mule.extension.api.introspection.DataType;
import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.declaration.fluent.DeclarationDescriptor;
import org.mule.extension.api.runtime.Interceptor;
import org.mule.extension.api.runtime.OperationExecutorFactory;
import org.mule.extension.api.runtime.source.Source;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * A simple pojo containing reference information for making test around a {@link DeclarationDescriptor}
 * which represents a theoretical &quot;Web Service Consumer&quot; extension.
 * <p>
 * It contains an actual {@link DeclarationDescriptor} that can be accessed through the {@link #getDescriptor()}
 * method plus some other getters which provides access to other declaration components
 * that you might want to make tests against
 *
 * @since 1.0
 */
public class TestWebServiceConsumerDeclarationReference
{

    public static final String CONFIG_NAME = "config";
    public static final String CONFIG_DESCRIPTION = "Default description";
    public static final String WS_CONSUMER = "WSConsumer";
    public static final String WS_CONSUMER_DESCRIPTION = "Generic Consumer for SOAP Web Services";
    public static final String VERSION = "3.6.0";
    public static final String WSDL_LOCATION = "wsdlLocation";
    public static final String URI_TO_FIND_THE_WSDL = "URI to find the WSDL";
    public static final String SERVICE = "service";
    public static final String SERVICE_NAME = "Service Name";
    public static final String PORT = "port";
    public static final String SERVICE_PORT = "Service Port";
    public static final String ADDRESS = "address";
    public static final String SERVICE_ADDRESS = "Service address";
    public static final String CONSUMER = "consumer";
    public static final String GO_GET_THEM_TIGER = "Go get them tiger";
    public static final String OPERATION = "operation";
    public static final String THE_OPERATION_TO_USE = "The operation to use";
    public static final String MTOM_ENABLED = "mtomEnabled";
    public static final String MTOM_DESCRIPTION = "Whether or not use MTOM for attachments";
    public static final String BROADCAST = "broadcast";
    public static final String BROADCAST_DESCRIPTION = "consumes many services";
    public static final String CALLBACK = "callback";
    public static final String CALLBACK_DESCRIPTION = "async callback";
    public static final String HAS_NO_ARGS = "has no args";
    public static final String ARG_LESS = "argLess";
    public static final String USERNAME = "username";
    public static final String USERNAME_DESCRIPTION = "Authentication username";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_DESCRIPTION = "Authentication password";
    public static final String MULESOFT = "MuleSoft";
    public static final String LISTENER = "listener";
    public static final String LISTEN_DESCRIPTION = "Listen requests";
    public static final String URL = "url";
    public static final String URL_DESCRIPTION = "Url to listen on";
    public static final String PORT_DESCRIPTION = "Port to listen on";
    public static final int DEFAULT_PORT = 8080;

    public static final String EXTENSION_MODEL_PROPERTY_KEY = "customExtensionModelProperty";
    public static final String CONFIGURATION_MODEL_PROPERTY_KEY = "customConfigurationModelProperty";
    public static final String OPERATION_MODEL_PROPERTY_KEY = "customOperationModelProperty";
    public static final String PARAMETER_MODEL_PROPERTY_KEY = "customParameterModelProperty";
    public static final String SOURCE_MODEL_PROPERTY_KEY = "customSourceModelProperty";

    public static final String EXTENSION_MODEL_PROPERTY_VALUE = "customExtensionModelPropertyValue";
    public static final String CONFIGURATION_MODEL_PROPERTY_VALUE = "customConfigurationModelPropertyValue";
    public static final String OPERATION_MODEL_PROPERTY_VALUE = "customOperationModelPropertyValue";
    public static final String PARAMETER_MODEL_PROPERTY_VALUE = "customParameterModelPropertyValue";
    public static final String SOURCE_MODEL_PROPERTY_VALUE = "customSourceModelPropertyValue";

    public static final String CONNECTION_PROVIDER_NAME = "connectionProvider";
    public static final String CONNECTION_PROVIDER_DESCRIPTION = "my connection provider";
    public static final Class<?> CONNECTION_PROVIDER_CONFIG_TYPE = String.class;
    public static final Class<?> CONNECTION_PROVIDER_CONNECTOR_TYPE = Integer.class;

    private final DeclarationDescriptor descriptor;
    private final ConfigurationFactory configurationFactory = mock(ConfigurationFactory.class);
    private final OperationExecutorFactory consumerExecutorFactory = mock(OperationExecutorFactory.class);
    private final OperationExecutorFactory broadcastExecutorFactory = mock(OperationExecutorFactory.class);
    private final OperationExecutorFactory argLessExecutorFactory = mock(OperationExecutorFactory.class);
    private final Interceptor configInterceptor1 = mock(Interceptor.class);
    private final Interceptor configInterceptor2 = mock(Interceptor.class);
    private final Interceptor operationInterceptor1 = mock(Interceptor.class);
    private final Interceptor operationInterceptor2 = mock(Interceptor.class);
    private final Interceptor messageSourceInterceptor1 = mock(Interceptor.class);
    private final Interceptor messageSourceInterceptor2 = mock(Interceptor.class);
    private final Source<Object, Serializable> source = mock(Source.class);
    private final ConnectionProviderFactory connectionProviderFactory = mock(ConnectionProviderFactory.class);
    private final Optional<ExceptionEnricherFactory> exceptionEnricherFactory = Optional.of(mock(ExceptionEnricherFactory.class));

    public TestWebServiceConsumerDeclarationReference()
    {
        descriptor = new DeclarationDescriptor();
        descriptor.named(WS_CONSUMER).describedAs(WS_CONSUMER_DESCRIPTION).onVersion(VERSION).fromVendor(MULESOFT)
                .withExceptionEnricherFactory(exceptionEnricherFactory)
                .withModelProperty(EXTENSION_MODEL_PROPERTY_KEY, EXTENSION_MODEL_PROPERTY_VALUE)
                .withConfig(CONFIG_NAME).createdWith(configurationFactory).describedAs(CONFIG_DESCRIPTION)
                .withModelProperty(CONFIGURATION_MODEL_PROPERTY_KEY, CONFIGURATION_MODEL_PROPERTY_VALUE)
                .withInterceptorFrom(() -> configInterceptor1)
                .withInterceptorFrom(() -> configInterceptor2)
                .with().requiredParameter(ADDRESS).describedAs(SERVICE_ADDRESS).ofType(String.class)
                .with().requiredParameter(PORT).describedAs(SERVICE_PORT).ofType(String.class)
                .with().requiredParameter(SERVICE).describedAs(SERVICE_NAME).ofType(String.class)
                .with().requiredParameter(WSDL_LOCATION).describedAs(URI_TO_FIND_THE_WSDL).ofType(String.class)
                .withExpressionSupport(NOT_SUPPORTED)
                .withModelProperty(PARAMETER_MODEL_PROPERTY_KEY, PARAMETER_MODEL_PROPERTY_VALUE)
                .withOperation(CONSUMER).describedAs(GO_GET_THEM_TIGER).executorsCreatedBy(consumerExecutorFactory)
                .whichReturns(DataType.of(InputStream.class))
                .withModelProperty(OPERATION_MODEL_PROPERTY_KEY, OPERATION_MODEL_PROPERTY_VALUE)
                .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(String.class)
                .with().optionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .withOperation(BROADCAST).describedAs(BROADCAST_DESCRIPTION).executorsCreatedBy(broadcastExecutorFactory)
                .withExceptionEnricherFactory(exceptionEnricherFactory)
                .whichReturns(DataType.of(void.class))
                .withInterceptorFrom(() -> operationInterceptor1)
                .withInterceptorFrom(() -> operationInterceptor2)
                .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(List.class, String.class)
                .with().optionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .with().requiredParameter(CALLBACK).describedAs(CALLBACK_DESCRIPTION).ofType(OperationModel.class).withExpressionSupport(REQUIRED)
                .withOperation(ARG_LESS).describedAs(HAS_NO_ARGS).executorsCreatedBy(argLessExecutorFactory)
                .whichReturns(DataType.of(int.class))
                .withConnectionProvider(CONNECTION_PROVIDER_NAME).describedAs(CONNECTION_PROVIDER_DESCRIPTION)
                .createdWith(connectionProviderFactory)
                .forConfigsOfType(CONNECTION_PROVIDER_CONFIG_TYPE)
                .whichGivesConnectionsOfType(CONNECTION_PROVIDER_CONNECTOR_TYPE)
                .with().requiredParameter(USERNAME).describedAs(USERNAME_DESCRIPTION).ofType(String.class)
                .with().requiredParameter(PASSWORD).describedAs(PASSWORD_DESCRIPTION).ofType(String.class)
                .withMessageSource(LISTENER).describedAs(LISTEN_DESCRIPTION).sourceCreatedBy(() -> source)
                .whichReturns(DataType.of(InputStream.class))
                .withAttributesOfType(DataType.of(Serializable.class))
                .withModelProperty(SOURCE_MODEL_PROPERTY_KEY, SOURCE_MODEL_PROPERTY_VALUE)
                .withInterceptorFrom(() -> messageSourceInterceptor1)
                .withInterceptorFrom(() -> messageSourceInterceptor2)
                .with().requiredParameter(URL).describedAs(URL_DESCRIPTION).ofType(String.class)
                .with().optionalParameter(PORT).describedAs(PORT_DESCRIPTION).ofType(Integer.class).defaultingTo(DEFAULT_PORT);
    }

    public DeclarationDescriptor getDescriptor()
    {
        return descriptor;
    }

    public ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    public OperationExecutorFactory getConsumerExecutorFactory()
    {
        return consumerExecutorFactory;
    }

    public OperationExecutorFactory getBroadcastExecutorFactory()
    {
        return broadcastExecutorFactory;
    }

    public OperationExecutorFactory getArgLessExecutorFactory()
    {
        return argLessExecutorFactory;
    }

    public Interceptor getConfigInterceptor1()
    {
        return configInterceptor1;
    }

    public Interceptor getConfigInterceptor2()
    {
        return configInterceptor2;
    }

    public Interceptor getOperationInterceptor1()
    {
        return operationInterceptor1;
    }

    public Interceptor getOperationInterceptor2()
    {
        return operationInterceptor2;
    }

    public ConnectionProviderFactory getConnectionProviderFactory()
    {
        return connectionProviderFactory;
    }

    public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory()
    {
        return exceptionEnricherFactory;
    }

    public Source getSource()
    {
        return source;
    }
}

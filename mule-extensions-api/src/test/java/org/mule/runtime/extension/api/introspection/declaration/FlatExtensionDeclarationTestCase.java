/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.Category.SELECT;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.ADDRESS;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.ARG_LESS;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.BROADCAST;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.BROADCAST_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CALLBACK;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CALLBACK_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONFIGURATION_MODEL_PROPERTY;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONFIG_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONFIG_NAME;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_CONNECTOR_TYPE;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_NAME;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.CONSUMER;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.DEFAULT_PORT;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.EXTENSION_MODEL_PROPERTY;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.GO_GET_THEM_TIGER;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.HAS_NO_ARGS;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.LISTENER;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.LISTEN_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.MIN_MULE_VERSION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.MTOM_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.MTOM_ENABLED;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.MULESOFT;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.OPERATION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.OPERATION_MODEL_PROPERTY;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.PARAMETER_MODEL_PROPERTY;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.PASSWORD;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.PASSWORD_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.PORT;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.PORT_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.SERVICE;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.SERVICE_ADDRESS;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.SERVICE_NAME;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.SERVICE_PORT;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.THE_OPERATION_TO_USE;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.URI_TO_FIND_THE_WSDL;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.URL;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.URL_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.USERNAME;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.USERNAME_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.VERSION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.WSDL_LOCATION;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.WS_CONSUMER;
import static org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer.WS_CONSUMER_DESCRIPTION;

import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.NullType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.declaration.fluent.BaseDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;
import org.mule.runtime.extension.tck.introspection.TestWebServiceConsumerDeclarer;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests the fluent API that allows
 * to perform declarations which describes an extension.
 *
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class FlatExtensionDeclarationTestCase extends BaseDeclarationTestCase
{

    private TestWebServiceConsumerDeclarer testDeclaration;
    private ExtensionDeclaration extensionDeclaration;

    @Before
    public void before()
    {
        testDeclaration = new TestWebServiceConsumerDeclarer();
        extensionDeclaration = testDeclaration.getExtensionDeclarer().getDeclaration();
    }

    @Test
    public void assertDeclaration()
    {
        assertThat(extensionDeclaration.getName(), is(WS_CONSUMER));
        assertThat(extensionDeclaration.getDescription(), is(WS_CONSUMER_DESCRIPTION));
        assertThat(extensionDeclaration.getVersion(), is(VERSION));
        assertThat(extensionDeclaration.getConfigurations(), hasSize(1));
        assertThat(extensionDeclaration.getVendor(), is(MULESOFT));
        assertThat(extensionDeclaration.getMinMuleVersion(), is(MIN_MULE_VERSION));
        assertThat(extensionDeclaration.getCategory(), is(SELECT));
        assertThat(extensionDeclaration.getExceptionEnricherFactory().isPresent(), is(true));
        assertThat(extensionDeclaration.getExceptionEnricherFactory().get(), is(sameInstance(testDeclaration.getExceptionEnricherFactory().get())));
        assertModelProperties(extensionDeclaration, EXTENSION_MODEL_PROPERTY);
    }

    @Test
    public void defaultConfiguration() throws Exception
    {
        assertThat(extensionDeclaration.getConfigurations(), hasSize(1));
        ConfigurationDeclaration configuration = extensionDeclaration.getConfigurations().get(0);
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getConfigurationFactory(), is(sameInstance(testDeclaration.getConfigurationFactory())));
        assertThat(configuration.getName(), is(CONFIG_NAME));
        assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));
        assertModelProperties(configuration, CONFIGURATION_MODEL_PROPERTY);

        List<InterceptorFactory> interceptorFactories = configuration.getInterceptorFactories();
        assertThat(interceptorFactories, is(notNullValue()));
        assertThat(interceptorFactories, hasSize(2));
        assertThat(interceptorFactories.get(0).createInterceptor(), is(sameInstance(testDeclaration.getConfigInterceptor1())));
        assertThat(interceptorFactories.get(1).createInterceptor(), is(sameInstance(testDeclaration.getConfigInterceptor2())));

        List<ParameterDeclaration> parameters = configuration.getParameters();
        assertThat(parameters, hasSize(4));
        assertParameter(parameters.get(0), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), PORT, SERVICE_PORT, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(2), SERVICE, SERVICE_NAME, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(3), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true, typeLoader.load(String.class), null);

        assertModelProperties(parameters.get(3), PARAMETER_MODEL_PROPERTY);
    }

    @Test
    public void operations() throws Exception
    {
        List<OperationDeclaration> operations = extensionDeclaration.getOperations();
        assertThat(operations, hasSize(3));
        assertConsumeOperation(operations);
        assertBroadcastOperation(operations);
        assertArgLessOperation(operations);
    }

    @Test
    public void connectionProvider() throws Exception
    {
        List<ConnectionProviderDeclaration> connectionProviders = extensionDeclaration.getConnectionProviders();
        assertThat(connectionProviders, hasSize(1));

        ConnectionProviderDeclaration connectionProvider = connectionProviders.get(0);
        assertThat(connectionProvider, is(notNullValue()));
        assertThat(connectionProvider.getName(), is(CONNECTION_PROVIDER_NAME));
        assertThat(connectionProvider.getDescription(), is(CONNECTION_PROVIDER_DESCRIPTION));
        assertThat(connectionProvider.getFactory(), is(sameInstance(testDeclaration.getConnectionProviderFactory())));
        assertThat(connectionProvider.getConnectionType(), is(sameInstance(CONNECTION_PROVIDER_CONNECTOR_TYPE)));

        List<ParameterDeclaration> parameters = connectionProvider.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
    }

    @Test
    public void messageSource() throws Exception
    {
        List<SourceDeclaration> sources = extensionDeclaration.getMessageSources();
        assertThat(sources, hasSize(1));

        SourceDeclaration source = sources.get(0);
        assertThat(source, is(notNullValue()));
        assertThat(source.getName(), is(LISTENER));
        assertThat(source.getDescription(), is(LISTEN_DESCRIPTION));
        assertThat(source.getSourceFactory().createSource(), is(sameInstance(testDeclaration.getSource())));
        assertDataType(source.getOutput().getType(), InputStream.class, BinaryType.class);
        assertDataType(source.getOutputAttributes().getType(), Serializable.class, ObjectType.class);

        List<ParameterDeclaration> parameters = source.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), URL, URL_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), PORT, PORT_DESCRIPTION, SUPPORTED, false, typeLoader.load(Integer.class), DEFAULT_PORT);
    }

    private void assertConsumeOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(0);
        assertThat(operation.getName(), is(CONSUMER));
        assertThat(operation.getDescription(), is(GO_GET_THEM_TIGER));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getConsumerExecutorFactory())));
        assertDataType(operation.getOutput().getType(), InputStream.class, BinaryType.class);
        assertDataType(operation.getOutputAttributes().getType(), String.class, StringType.class);
        assertModelProperties(operation, OPERATION_MODEL_PROPERTY);

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class), true);
        assertThat(operation.getInterceptorFactories(), is(empty()));
    }

    private void assertBroadcastOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(1);

        assertThat(operation.getName(), is(BROADCAST));
        assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getBroadcastExecutorFactory())));
        assertDataType(operation.getOutput().getType(), void.class, NullType.class);

        Optional<ExceptionEnricherFactory> exceptionEnricherFactory = operation.getExceptionEnricherFactory();
        assertThat(exceptionEnricherFactory.isPresent(), is(true));
        assertThat(exceptionEnricherFactory.get(), is(sameInstance(testDeclaration.getExceptionEnricherFactory().get())));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(3));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, typeBuilder.arrayType()
                                .id(List.class.getName())
                                .of(typeBuilder.stringType().id(String.class.getName()))
                                .build()
                , null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class), true);
        assertParameter(parameters.get(2), CALLBACK, CALLBACK_DESCRIPTION, REQUIRED, true, typeLoader.load(OperationModel.class), null);

        List<InterceptorFactory> interceptorFactories = operation.getInterceptorFactories();
        assertThat(operation.getInterceptorFactories(), hasSize(2));
        assertThat(interceptorFactories.get(0).createInterceptor(), is(sameInstance(testDeclaration.getOperationInterceptor1())));
        assertThat(interceptorFactories.get(1).createInterceptor(), is(sameInstance(testDeclaration.getOperationInterceptor2())));
    }

    private void assertArgLessOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(2);

        assertThat(operation.getName(), is(ARG_LESS));
        assertThat(operation.getDescription(), is(HAS_NO_ARGS));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getArgLessExecutorFactory())));
        assertDataType(operation.getOutput().getType(), int.class, NumberType.class);

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, is(notNullValue()));
        assertThat(parameters.isEmpty(), is(true));
        assertThat(operation.getInterceptorFactories(), is(empty()));
    }

    private void assertModelProperties(BaseDeclaration<?> declaration, ModelProperty... modelProperty)
    {
        List<ModelProperty> expected = Arrays.asList(modelProperty);
        Set<ModelProperty> properties = declaration.getModelProperties();
        assertThat(properties, is(not(nullValue())));
        assertThat(properties.size(), is(expected.size()));
        expected.forEach(mp -> {
            assertThat(properties.contains(mp), is(true));
            assertThat(declaration.getModelProperty(mp.getClass()).get(), is(sameInstance(mp)));
        });
    }
}

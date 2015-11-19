/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.extension.api.introspection.DataQualifier.BOOLEAN;
import static org.mule.extension.api.introspection.DataQualifier.INTEGER;
import static org.mule.extension.api.introspection.DataQualifier.LIST;
import static org.mule.extension.api.introspection.DataQualifier.POJO;
import static org.mule.extension.api.introspection.DataQualifier.STRING;
import static org.mule.extension.api.introspection.DataQualifier.VOID;
import static org.mule.extension.api.introspection.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.extension.api.introspection.ExpressionSupport.REQUIRED;
import static org.mule.extension.api.introspection.ExpressionSupport.SUPPORTED;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.ADDRESS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.ARG_LESS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.BROADCAST;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.BROADCAST_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CALLBACK;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CALLBACK_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONFIGURATION_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONFIGURATION_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONFIG_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONFIG_NAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONNECTION_PROVIDER_CONFIG_TYPE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONNECTION_PROVIDER_CONNECTOR_TYPE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONNECTION_PROVIDER_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONNECTION_PROVIDER_NAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.CONSUMER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.EXTENSION_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.EXTENSION_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.GO_GET_THEM_TIGER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.HAS_NO_ARGS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.MTOM_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.MTOM_ENABLED;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PARAMETER_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PARAMETER_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PASSWORD;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PASSWORD_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PORT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_ADDRESS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_NAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_PORT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.THE_OPERATION_TO_USE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.URI_TO_FIND_THE_WSDL;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.USERNAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.USERNAME_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.VERSION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WSDL_LOCATION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WS_CONSUMER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WS_CONSUMER_DESCRIPTION;
import org.mule.extension.api.introspection.DataQualifier;
import org.mule.extension.api.introspection.DataType;
import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.declaration.fluent.BaseDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ConfigurationDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.Declaration;
import org.mule.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ParameterDeclaration;
import org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference;
import org.mule.extension.api.runtime.InterceptorFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
public class DeclarationTestCase
{

    private TestWebServiceConsumerDeclarationReference testDeclaration;
    private Declaration declaration;

    @Before
    public void before()
    {
        testDeclaration = new TestWebServiceConsumerDeclarationReference();
        declaration = testDeclaration.getDescriptor().getRootDeclaration().getDeclaration();
    }

    @Test
    public void assertDeclaration()
    {
        assertThat(declaration.getName(), is(WS_CONSUMER));
        assertThat(declaration.getDescription(), is(WS_CONSUMER_DESCRIPTION));
        assertThat(declaration.getVersion(), is(VERSION));
        assertThat(declaration.getConfigurations(), hasSize(1));
        assertModelProperties(declaration, EXTENSION_MODEL_PROPERTY_KEY, EXTENSION_MODEL_PROPERTY_VALUE);
    }

    @Test
    public void defaultConfiguration() throws Exception
    {
        assertThat(declaration.getConfigurations(), hasSize(1));
        ConfigurationDeclaration configuration = declaration.getConfigurations().get(0);
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getConfigurationFactory(), is(sameInstance(testDeclaration.getConfigurationFactory())));
        assertThat(configuration.getName(), is(CONFIG_NAME));
        assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));
        assertModelProperties(configuration, CONFIGURATION_MODEL_PROPERTY_KEY, CONFIGURATION_MODEL_PROPERTY_VALUE);

        List<InterceptorFactory> interceptorFactories = configuration.getInterceptorFactories();
        assertThat(interceptorFactories, is(notNullValue()));
        assertThat(interceptorFactories, hasSize(2));
        assertThat(interceptorFactories.get(0).createInterceptor(), is(sameInstance(testDeclaration.getConfigInterceptor1())));
        assertThat(interceptorFactories.get(1).createInterceptor(), is(sameInstance(testDeclaration.getConfigInterceptor2())));

        List<ParameterDeclaration> parameters = configuration.getParameters();
        assertThat(parameters, hasSize(4));
        assertParameter(parameters.get(0), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), PORT, SERVICE_PORT, SUPPORTED, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(2), SERVICE, SERVICE_NAME, SUPPORTED, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(3), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true, DataType.of(String.class), STRING, null);

        assertModelProperties(parameters.get(3), PARAMETER_MODEL_PROPERTY_KEY, PARAMETER_MODEL_PROPERTY_VALUE);
    }

    @Test
    public void operations() throws Exception
    {
        List<OperationDeclaration> operations = declaration.getOperations();
        assertThat(operations, hasSize(3));
        assertConsumeOperation(operations);
        assertBroadcastOperation(operations);
        assertArgLessOperation(operations);
    }

    @Test
    public void connectionProvider() throws Exception
    {
        List<ConnectionProviderDeclaration> connectionProviders = declaration.getConnectionProviders();
        assertThat(connectionProviders, hasSize(1));

        ConnectionProviderDeclaration connectionProvider = connectionProviders.get(0);
        assertThat(connectionProvider, is(notNullValue()));
        assertThat(connectionProvider.getName(), is(CONNECTION_PROVIDER_NAME));
        assertThat(connectionProvider.getDescription(), is(CONNECTION_PROVIDER_DESCRIPTION));
        assertThat(connectionProvider.getFactory(), is(sameInstance(testDeclaration.getConnectionProviderFactory())));
        assertThat(connectionProvider.getConfigurationType(), is(sameInstance(CONNECTION_PROVIDER_CONFIG_TYPE)));
        assertThat(connectionProvider.getConnectionType(), is(sameInstance(CONNECTION_PROVIDER_CONNECTOR_TYPE)));

        List<ParameterDeclaration> parameters = connectionProvider.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, DataType.of(String.class), STRING, null);
    }

    private void assertConsumeOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(0);
        assertThat(operation.getName(), is(CONSUMER));
        assertThat(operation.getDescription(), is(GO_GET_THEM_TIGER));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getConsumerExecutorFactory())));
        assertDataType(operation.getReturnType(), InputStream.class, POJO);
        assertModelProperties(operation, OPERATION_MODEL_PROPERTY_KEY, OPERATION_MODEL_PROPERTY_VALUE);

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, DataType.of(Boolean.class), BOOLEAN, true);
        assertThat(operation.getInterceptorFactories(), is(empty()));
    }

    private void assertBroadcastOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(1);

        assertThat(operation.getName(), is(BROADCAST));
        assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getBroadcastExecutorFactory())));
        assertDataType(operation.getReturnType(), void.class, VOID);

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(3));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, DataType.of(List.class, String.class), LIST, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, DataType.of(Boolean.class), BOOLEAN, true);
        assertParameter(parameters.get(2), CALLBACK, CALLBACK_DESCRIPTION, REQUIRED, true, DataType.of(OperationModel.class), DataQualifier.OPERATION, null);

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
        assertDataType(operation.getReturnType(), int.class, INTEGER);

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, is(notNullValue()));
        assertThat(parameters.isEmpty(), is(true));
        assertThat(operation.getInterceptorFactories(), is(empty()));
    }

    private void assertParameter(ParameterDeclaration parameter,
                                 String name,
                                 String description,
                                 ExpressionSupport expressionSupport,
                                 boolean required,
                                 DataType type,
                                 DataQualifier qualifier,
                                 Object defaultValue)
    {

        assertThat(parameter, is(notNullValue()));
        assertThat(parameter.getName(), is(name));
        assertThat(parameter.getDescription(), is(description));
        assertThat(parameter.getExpressionSupport(), is(expressionSupport));
        assertThat(parameter.isRequired(), is(required));
        assertThat(parameter.getDefaultValue(), equalTo(defaultValue));
        assertThat(parameter.getType(), equalTo(type));
        assertSame(qualifier, parameter.getType().getQualifier());
    }

    private void assertModelProperties(BaseDeclaration<?> declaration, String key, Object value)
    {
        Map<String, Object> properties = declaration.getModelProperties();
        assertThat(properties, is(not(nullValue())));
        assertThat(properties.size(), is(1));
        assertThat(properties.containsKey(key), is(true));
        assertThat(properties.get(key), is(sameInstance(value)));
    }

    private void assertDataType(DataType dataType, Class<?> expectedRawType, DataQualifier expectedQualifier)
    {
        assertThat(dataType.getRawType(), equalTo(expectedRawType));
        assertThat(dataType.getQualifier(), is(expectedQualifier));
    }
}

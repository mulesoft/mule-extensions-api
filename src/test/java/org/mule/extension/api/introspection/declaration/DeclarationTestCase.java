/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.declaration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
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
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.DEFAULT_PORT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.EXTENSION_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.EXTENSION_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.GO_GET_THEM_TIGER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.HAS_NO_ARGS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.LISTENER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.LISTEN_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.MTOM_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.MTOM_ENABLED;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.MULESOFT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.OPERATION_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PARAMETER_MODEL_PROPERTY_KEY;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PARAMETER_MODEL_PROPERTY_VALUE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PASSWORD;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PASSWORD_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PORT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.PORT_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_ADDRESS;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_NAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.SERVICE_PORT;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.THE_OPERATION_TO_USE;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.URI_TO_FIND_THE_WSDL;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.URL;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.URL_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.USERNAME;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.USERNAME_DESCRIPTION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.VERSION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WSDL_LOCATION;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WS_CONSUMER;
import static org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference.WS_CONSUMER_DESCRIPTION;
import static org.mule.metadata.java.JavaTypeLoader.JAVA;
import org.mule.extension.api.introspection.ExceptionEnricherFactory;
import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.declaration.fluent.BaseDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ConfigurationDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.Declaration;
import org.mule.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.ParameterDeclaration;
import org.mule.extension.api.introspection.declaration.fluent.SourceDeclaration;
import org.mule.extension.api.introspection.declaration.tck.TestWebServiceConsumerDeclarationReference;
import org.mule.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.extension.api.runtime.InterceptorFactory;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private BaseTypeBuilder<?> typeBuilder;
    private ClassTypeLoader typeLoader;

    @Before
    public void before()
    {
        testDeclaration = new TestWebServiceConsumerDeclarationReference();
        declaration = testDeclaration.getDescriptor().getRootDeclaration().getDeclaration();
        typeBuilder = BaseTypeBuilder.create(JAVA);
        typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader(getClass().getClassLoader());
    }

    @Test
    public void assertDeclaration()
    {
        assertThat(declaration.getName(), is(WS_CONSUMER));
        assertThat(declaration.getDescription(), is(WS_CONSUMER_DESCRIPTION));
        assertThat(declaration.getVersion(), is(VERSION));
        assertThat(declaration.getConfigurations(), hasSize(1));
        assertThat(declaration.getVendor(), is(MULESOFT));
        assertThat(declaration.getExceptionEnricherFactory().isPresent(), is(true));
        assertThat(declaration.getExceptionEnricherFactory().get(), is(sameInstance(testDeclaration.getExceptionEnricherFactory().get())));
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
        assertParameter(parameters.get(0), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), PORT, SERVICE_PORT, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(2), SERVICE, SERVICE_NAME, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(3), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true, typeLoader.load(String.class), null);

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
        assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
        assertParameter(parameters.get(1), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
    }

    @Test
    public void messageSource() throws Exception
    {
        List<SourceDeclaration> sources = declaration.getMessageSources();
        assertThat(sources, hasSize(1));

        SourceDeclaration source = sources.get(0);
        assertThat(source, is(notNullValue()));
        assertThat(source.getName(), is(LISTENER));
        assertThat(source.getDescription(), is(LISTEN_DESCRIPTION));
        assertThat(source.getSourceFactory().createSource(), is(sameInstance(testDeclaration.getSource())));
        assertDataType(source.getReturnType(), InputStream.class, BinaryType.class);
        assertDataType(source.getAttributesType(), Serializable.class, ObjectType.class);

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
        assertDataType(operation.getReturnType(), InputStream.class, BinaryType.class);
        assertModelProperties(operation, OPERATION_MODEL_PROPERTY_KEY, OPERATION_MODEL_PROPERTY_VALUE);

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
        assertDataType(operation.getReturnType(), void.class, NullType.class);

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
        assertDataType(operation.getReturnType(), int.class, NumberType.class);

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
                                 MetadataType type,
                                 Object defaultValue)
    {
        assertThat(parameter, is(notNullValue()));
        assertThat(parameter.getName(), is(name));
        assertThat(parameter.getDescription(), is(description));
        assertThat(parameter.getExpressionSupport(), is(expressionSupport));
        assertThat(parameter.isRequired(), is(required));
        assertThat(parameter.getDefaultValue(), equalTo(defaultValue));
        assertThat(parameter.getType(), equalTo(type));
    }

    private void assertModelProperties(BaseDeclaration<?> declaration, String key, Object value)
    {
        Map<String, Object> properties = declaration.getModelProperties();
        assertThat(properties, is(not(nullValue())));
        assertThat(properties.size(), is(1));
        assertThat(properties.containsKey(key), is(true));
        assertThat(properties.get(key), is(sameInstance(value)));
    }

    private void assertDataType(MetadataType type, Class<?> expectedRawType, Class<? extends MetadataType> typeQualifier)
    {
        assertThat(type, is(instanceOf(typeQualifier)));

        if (type instanceof NullType)
        {
            return;
        }

        Collection<TypeIdAnnotation> annotations = type.getAnnotation(TypeIdAnnotation.class);
        assertThat(annotations, hasSize(1));
        TypeIdAnnotation typeIdAnnotation = annotations.iterator().next();
        assertThat(expectedRawType.getName(), is(equalTo(typeIdAnnotation.getValue())));
    }
}

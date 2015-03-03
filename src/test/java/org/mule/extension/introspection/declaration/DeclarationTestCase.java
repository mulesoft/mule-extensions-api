/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mule.extension.introspection.DataQualifier.BOOLEAN;
import static org.mule.extension.introspection.DataQualifier.LIST;
import static org.mule.extension.introspection.DataQualifier.STRING;
import org.mule.extension.introspection.ConfigurationInstantiator;
import org.mule.extension.introspection.DataQualifier;
import org.mule.extension.introspection.DataType;
import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.OperationImplementation;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests the fluent API that allows
 * to perform declarations which describes an extension.
 * <p/>
 * It contains some public constants and public methods
 * to help implementation perform their own tests with some
 * test data.
 * <p/>
 * This is a 'good guy' deal only. It's just an attempt to help.
 * No backwards compatibility commitment is taken on this class
 * or any of its members
 *
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class DeclarationTestCase
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
    private static ConfigurationInstantiator configurationInstantiator;
    private static OperationImplementation consumerImplementation;
    private static OperationImplementation broadcastImplementation;
    private static OperationImplementation argLessImplementation;
    private static Object capability = new Object();

    private Declaration declaration;

    public static DeclarationConstruct createConstruct()
    {
        return new DeclarationTestCase().doCreateConstruct();
    }

    private DeclarationConstruct doCreateConstruct()
    {
        configurationInstantiator = mock(ConfigurationInstantiator.class);
        consumerImplementation = mock(OperationImplementation.class);
        broadcastImplementation = mock(OperationImplementation.class);
        argLessImplementation = mock(OperationImplementation.class);


        DeclarationConstruct construct = new DeclarationConstruct(WS_CONSUMER, VERSION).describedAs(WS_CONSUMER_DESCRIPTION);

        construct
                .withCapability(capability)
                .withConfig(CONFIG_NAME).instantiatedWith(configurationInstantiator).describedAs(CONFIG_DESCRIPTION)
                .with().requiredParameter(WSDL_LOCATION).describedAs(URI_TO_FIND_THE_WSDL).ofType(String.class).whichIsNotDynamic()
                .with().requiredParameter(SERVICE).describedAs(SERVICE_NAME).ofType(String.class)
                .with().requiredParameter(PORT).describedAs(SERVICE_PORT).ofType(String.class).withCapability(capability)
                .with().requiredParameter(ADDRESS).describedAs(SERVICE_ADDRESS).ofType(String.class)
                .withOperation(CONSUMER).describedAs(GO_GET_THEM_TIGER).implementedIn(consumerImplementation).withCapability(capability)
                .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(String.class).withCapability(capability)
                .with().optionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .withOperation(BROADCAST).describedAs(BROADCAST_DESCRIPTION).implementedIn(broadcastImplementation)
                .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(List.class, String.class)
                .with().optionalParameter(MTOM_ENABLED).whichIsDynamic().describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .with().requiredParameter(CALLBACK).describedAs(CALLBACK_DESCRIPTION).whichIsNotDynamic().ofType(Operation.class)
                .withOperation(ARG_LESS).describedAs(HAS_NO_ARGS).implementedIn(argLessImplementation);

        return construct;
    }

    @Before
    public void buildDeclaration() throws Exception
    {
        declaration = createConstruct().getRootConstruct().getDeclaration();
    }

    @Test
    public void assertDeclaration()
    {
        assertThat(declaration.getName(), is(WS_CONSUMER));
        assertThat(declaration.getDescription(), is(WS_CONSUMER_DESCRIPTION));
        assertThat(declaration.getVersion(), is(VERSION));
        assertThat(declaration.getConfigurations(), hasSize(1));

        Set<Object> capabilities = declaration.getCapabilities();
        assertThat(capabilities, is(notNullValue()));
        assertThat(capabilities, hasSize(1));
        assertThat(capabilities, contains(capability));
    }

    @Test
    public void defaultConfiguration() throws Exception
    {
        assertThat(declaration.getConfigurations(), hasSize(1));
        ConfigurationDeclaration configuration = declaration.getConfigurations().get(0);
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getConfigurationInstantiator(), is(sameInstance(configurationInstantiator)));
        assertThat(configuration.getName(), is(CONFIG_NAME));
        assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));

        List<ParameterDeclaration> parameters = configuration.getParameters();
        assertThat(parameters, hasSize(4));
        assertParameter(parameters.get(0), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, false, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), SERVICE, SERVICE_NAME, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(2), PORT, SERVICE_PORT, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(3), ADDRESS, SERVICE_ADDRESS, true, true, DataType.of(String.class), STRING, null);
        assertThat(parameters.get(2).getCapabilities(), contains(capability));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCapability()
    {
        createConstruct().withCapability(null);
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

    private void assertConsumeOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(0);
        assertThat(operation.getName(), is(CONSUMER));
        assertThat(operation.getDescription(), is(GO_GET_THEM_TIGER));
        assertThat(operation.getImplementation(), is(sameInstance(consumerImplementation)));
        assertThat(operation.getCapabilities(), contains(capability));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, true, false, DataType.of(Boolean.class), BOOLEAN, true);
        assertThat(parameters.get(0).getCapabilities(), contains(capability));
    }

    private void assertBroadcastOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(1);

        assertThat(operation.getName(), is(BROADCAST));
        assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));
        assertThat(operation.getImplementation(), is(sameInstance(broadcastImplementation)));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(3));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, true, true, DataType.of(List.class, String.class), LIST, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, true, false, DataType.of(Boolean.class), BOOLEAN, true);
        assertParameter(parameters.get(2), CALLBACK, CALLBACK_DESCRIPTION, false, true, DataType.of(Operation.class), DataQualifier.OPERATION, null);
    }

    private void assertArgLessOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(2);

        assertThat(operation.getName(), is(ARG_LESS));
        assertThat(operation.getDescription(), is(HAS_NO_ARGS));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, is(notNullValue()));
        assertThat(parameters.isEmpty(), is(true));
    }

    private void assertParameter(ParameterDeclaration parameter,
                                 String name,
                                 String description,
                                 boolean acceptsExpressions,
                                 boolean required,
                                 DataType type,
                                 DataQualifier qualifier,
                                 Object defaultValue)
    {

        assertThat(parameter, is(notNullValue()));
        assertThat(parameter.getName(), is(name));
        assertThat(parameter.getDescription(), is(description));
        assertThat(parameter.isDynamic(), is(acceptsExpressions));
        assertThat(parameter.isRequired(), is(required));
        assertThat(parameter.getDefaultValue(), equalTo(defaultValue));
        assertThat(parameter.getType(), equalTo(type));
        assertSame(qualifier, parameter.getType().getQualifier());
    }
}

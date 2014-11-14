/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.declaration;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.extensions.introspection.DataQualifier.BOOLEAN;
import static org.mule.extensions.introspection.DataQualifier.LIST;
import static org.mule.extensions.introspection.DataQualifier.STRING;
import org.mule.extensions.introspection.DataQualifier;
import org.mule.extensions.introspection.DataType;
import org.mule.extensions.introspection.Operation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DeclarationTestCase
{

    private static final String CONFIG_NAME = "config";
    private static final String CONFIG_DESCRIPTION = "Default description";
    private static final String WS_CONSUMER = "WSConsumer";
    private static final String WS_CONSUMER_DESCRIPTION = "Generic Consumer for SOAP Web Services";
    private static final String VERSION = "3.6.0";
    private static final String WSDL_LOCATION = "wsdlLocation";
    private static final String URI_TO_FIND_THE_WSDL = "URI to find the WSDL";
    private static final String SERVICE = "service";
    private static final String SERVICE_NAME = "Service Name";
    private static final String PORT = "port";
    private static final String SERVICE_PORT = "Service Port";
    private static final String ADDRESS = "address";
    private static final String SERVICE_ADDRESS = "Service address";
    private static final String CONSUMER = "consumer";
    private static final String GO_GET_THEM_TIGER = "Go get them tiger";
    private static final String OPERATION = "operation";
    private static final String THE_OPERATION_TO_USE = "The operation to use";
    private static final String MTOM_ENABLED = "mtomEnabled";
    private static final String MTOM_DESCRIPTION = "Whether or not use MTOM for attachments";
    private static final String BROADCAST = "broadcast";
    private static final String BROADCAST_DESCRIPTION = "consumes many services";
    private static final String CALLBACK = "callback";
    private static final String CALLBACK_DESCRIPTION = "async callback";
    private static final String HAS_NO_ARGS = "has no args";
    private static final String ARG_LESS = "argLess";

    private Declaration declaration;

    private DeclarationConstruct createConstruct()
    {
        DeclarationConstruct construct = new DeclarationConstruct(WS_CONSUMER, VERSION).describedAs(WS_CONSUMER_DESCRIPTION);

        construct
                .withCapability(new Date())
                .withConfig(CONFIG_NAME).declaredIn(WsConsumerConfig.class).describedAs(CONFIG_DESCRIPTION)
                    .with().requiredParameter(WSDL_LOCATION).describedAs(URI_TO_FIND_THE_WSDL).ofType(String.class).whichIsNotDynamic()
                    .with().requiredParameter(SERVICE).describedAs(SERVICE_NAME).ofType(String.class)
                    .with().requiredParameter(PORT).describedAs(SERVICE_PORT).ofType(String.class)
                    .with().requiredParameter(ADDRESS).describedAs(SERVICE_ADDRESS).ofType(String.class)
                .withOperation(CONSUMER).describedAs(GO_GET_THEM_TIGER)
                    .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(String.class)
                    .with().optionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .withOperation(BROADCAST).describedAs(BROADCAST_DESCRIPTION)
                    .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(List.class, String.class)
                    .with().optionalParameter(MTOM_ENABLED).whichIsDynamic().describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                    .with().requiredParameter(CALLBACK).describedAs(CALLBACK_DESCRIPTION).whichIsNotDynamic().ofType(Operation.class)
                .withOperation(ARG_LESS).describedAs(HAS_NO_ARGS);

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
        Object capability = capabilities.iterator().next();
        assertThat(capability, instanceOf(Date.class));
    }

    @Test
    public void defaultConfiguration() throws Exception
    {
        assertThat(declaration.getConfigurations(), hasSize(1));
        ConfigurationDeclaration configuration = declaration.getConfigurations().get(0);
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getName(), is(CONFIG_NAME));
        assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));

        List<ParameterDeclaration> parameters = configuration.getParameters();
        assertThat(parameters, hasSize(4));
        assertParameter(parameters.get(0), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, false, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), SERVICE, SERVICE_NAME, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(2), PORT, SERVICE_PORT, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(3), ADDRESS, SERVICE_ADDRESS, true, true, DataType.of(String.class), STRING, null);
    }

    //@Test
    //public void onlyOneConfig() throws Exception
    //{
    //    assertThat(declaration.getConfiguration(CONFIG_NAME), is (config.get(0))
    //    assertSame(declaration.getConfigurations().get(0), declaration.getConfiguration(CONFIG_NAME));
    //}

    //@Test(expected = NoSuchConfigurationException.class)
    //public void noSuchConfiguration() throws Exception
    //{
    //    declaration.getConfiguration("fake");
    //}
    //
    //@Test(expected = NoSuchOperationException.class)
    //public void noSuchOperation() throws Exception
    //{
    //    declaration.getOperation("fake");
    //}

    //@Test
    //public void noSuchCapability()
    //{
    //    Set<String> capabilities = declaration.getCapabilities(String.class);
    //    assertNotNull(capabilities);
    //    assertTrue(capabilities.isEmpty());
    //}

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

    //@Test(expected = IllegalArgumentException.class)
    //public void badExtensionVersion()
    //{
    //    builder.setVersion("i'm new").build();
    //}

    //@Test
    //public void configurationsOrder()
    //{
    //    final String beta = "beta";
    //    final String alpha = "alpha";
    //
    //    Extension extension = builder
    //            .addConfiguration(builder.newConfiguration()
    //                                      .setName(beta)
    //                                      .setDescription(beta)
    //                                      .setDeclaringClass(DECLARING_CLASS))
    //            .addConfiguration(builder.newConfiguration()
    //                                      .setName(alpha)
    //                                      .setDescription(alpha)
    //                                      .setDeclaringClass(DECLARING_CLASS))
    //            .build();
    //
    //    List<Configuration> configurations = declaration.getConfigurations();
    //    assertEquals(3, configurations.size());
    //    assertEquals(CONFIG_NAME, configurations.get(0).getName());
    //    assertEquals(alpha, configurations.get(1).getName());
    //    assertEquals(beta, configurations.get(2).getName());
    //}

    //@Test
    //public void operationsAlphaSorted()
    //{
    //    assertEquals(2, declaration.getOperations().size());
    //    assertEquals(BROADCAST, declaration.getOperations().get(0).getName());
    //    assertEquals(CONSUMER, declaration.getOperations().get(1).getName());
    //}

    //@Test(expected = IllegalArgumentException.class)
    //public void nameClashes()
    //{
    //    builder.addOperation(builder.newOperation()
    //                                 .setName(CONFIG_NAME)
    //                                 .setDescription(""))
    //            .build();
    //}

    //@Test(expected = IllegalArgumentException.class)
    //public void operationWithParameterNamedName()
    //{
    //    builder.addOperation(builder.newOperation()
    //                                 .setName("invalidOperation")
    //                                 .setDescription("")
    //                                 .addParameter(builder.newParameter()
    //                                                       .setName("name")
    //                                                       .setType(STRING_DATA_TYPE)))
    //            .build();
    //}


    //@Test(expected = IllegalArgumentException.class)
    //public void nameWithSpaces()
    //{
    //    builder.setName("i have spaces").build();
    //}
    //
    //@Test(expected = IllegalArgumentException.class)
    //public void configurationWithFiledWithoutGetter()
    //{
    //    builder.addConfiguration(builder.newConfiguration()
    //                                     .setName("fail")
    //                                     .setDescription("fail")
    //                                     .setDeclaringClass(getClass())
    //                                     .addParameter(builder.newParameter()
    //                                                           .setName("notExistent")
    //                                                           .setType(STRING_DATA_TYPE)
    //                                                           .setDescription("no setter")))
    //            .build();
    //}
    //
    //
    //@Test(expected = IllegalArgumentException.class)
    //public void configurationWithoutValidConstructor()
    //{
    //    builder.addConfiguration(builder.newConfiguration()
    //                                     .setName("fail")
    //                                     .setDescription("fail")
    //                                     .setDeclaringClass(InvalidConstructorConfiguration.class))
    //            .build();
    //}

    private void assertConsumeOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(0);
        assertThat(operation.getName(), is(CONSUMER));
        assertThat(operation.getDescription(), is(GO_GET_THEM_TIGER));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, true, false, DataType.of(Boolean.class), BOOLEAN, true);
    }

    private void assertBroadcastOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(1);

        assertThat(operation.getName(), is(BROADCAST));
        assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));

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

    private void strictTypeAssert(List<DataType> types, Class<?> expected, Class<?>[]... genericTypes)
    {
        assertEquals(1, types.size());
        strictTypeAssert(types.get(0), expected, genericTypes);
    }

    private void strictTypeAssert(DataType type, Class<?> expected, Class<?>[]... genericTypes)
    {
        assertEquals(expected, type.getRawType());
        Arrays.equals(genericTypes, type.getGenericTypes());
    }

    @SuppressWarnings("unused")
    private static class WsConsumerConfig
    {

        public WsConsumerConfig()
        {

        }

        public void setWsdlLocation(String wsdlLocation)
        {

        }

        public void setService(String service)
        {

        }

        public void setPort(String port)
        {

        }

        public void setAddress(String address)
        {

        }
    }

    private static class InvalidConstructorConfiguration
    {

        @SuppressWarnings("unused")
        public InvalidConstructorConfiguration(String value)
        {
        }
    }
}

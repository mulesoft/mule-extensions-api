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
import static org.mule.extension.introspection.DataQualifier.BOOLEAN;
import static org.mule.extension.introspection.DataQualifier.LIST;
import static org.mule.extension.introspection.DataQualifier.STRING;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.ADDRESS;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.ARG_LESS;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.BROADCAST;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.BROADCAST_DESCRIPTION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.CALLBACK;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.CALLBACK_DESCRIPTION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.CONFIG_DESCRIPTION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.CONFIG_NAME;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.CONSUMER;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.GO_GET_THEM_TIGER;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.HAS_NO_ARGS;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.MTOM_DESCRIPTION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.MTOM_ENABLED;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.OPERATION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.PORT;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.SERVICE;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.SERVICE_ADDRESS;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.SERVICE_NAME;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.SERVICE_PORT;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.THE_OPERATION_TO_USE;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.URI_TO_FIND_THE_WSDL;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.VERSION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.WSDL_LOCATION;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.WS_CONSUMER;
import static org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference.WS_CONSUMER_DESCRIPTION;
import org.mule.extension.introspection.DataQualifier;
import org.mule.extension.introspection.DataType;
import org.mule.extension.introspection.Operation;
import org.mule.extension.introspection.declaration.tck.WebServiceConsumerTestDeclarationReference;

import java.util.List;
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
public class DeclarationTestCase extends CapableDeclarationContractTestCase<Declaration>
{

    private WebServiceConsumerTestDeclarationReference testDeclaration;

    @Before
    public void before()
    {
        testDeclaration = new WebServiceConsumerTestDeclarationReference();
        declaration = createDeclaration();
    }

    @Override
    protected Declaration createDeclaration()
    {
        return testDeclaration.getDescriptor().getRootDeclaration().getDeclaration();
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
        assertThat(capabilities, contains(testDeclaration.getCapability()));
    }

    @Test
    public void defaultConfiguration() throws Exception
    {
        assertThat(declaration.getConfigurations(), hasSize(1));
        ConfigurationDeclaration configuration = declaration.getConfigurations().get(0);
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getConfigurationInstantiator(), is(sameInstance(testDeclaration.getConfigurationInstantiator())));
        assertThat(configuration.getName(), is(CONFIG_NAME));
        assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));

        List<ParameterDeclaration> parameters = configuration.getParameters();
        assertThat(parameters, hasSize(4));
        assertParameter(parameters.get(0), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, false, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), SERVICE, SERVICE_NAME, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(2), PORT, SERVICE_PORT, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(3), ADDRESS, SERVICE_ADDRESS, true, true, DataType.of(String.class), STRING, null);
        assertThat(parameters.get(2).getCapabilities(), contains(testDeclaration.getCapability()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCapability()
    {
        testDeclaration.getDescriptor().withCapability(null);
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
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getConsumerExecutorFactory())));
        assertThat(operation.getCapabilities(), contains(testDeclaration.getCapability()));

        List<ParameterDeclaration> parameters = operation.getParameters();
        assertThat(parameters, hasSize(2));
        assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, true, true, DataType.of(String.class), STRING, null);
        assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, true, false, DataType.of(Boolean.class), BOOLEAN, true);
        assertThat(parameters.get(0).getCapabilities(), contains(testDeclaration.getCapability()));
    }

    private void assertBroadcastOperation(List<OperationDeclaration> operations)
    {
        OperationDeclaration operation = operations.get(1);

        assertThat(operation.getName(), is(BROADCAST));
        assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));
        assertThat(operation.getExecutorFactory(), is(sameInstance(testDeclaration.getBroadcastExecutorFactory())));

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

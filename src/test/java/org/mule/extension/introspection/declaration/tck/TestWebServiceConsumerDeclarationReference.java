/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration.tck;

import static org.mockito.Mockito.mock;
import org.mule.extension.introspection.ConfigurationInstantiator;
import org.mule.extension.introspection.OperationModel;
import org.mule.extension.introspection.declaration.fluent.DeclarationDescriptor;
import org.mule.extension.introspection.declaration.fluent.OperationExecutorFactory;
import org.mule.extension.runtime.Interceptor;

import java.util.List;

/**
 * A simple pojo containing reference information for making test around a {@link DeclarationDescriptor}
 * which represents a theoretical &quot;Web Service Consumer&quot; extension.
 *
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

    public static final String EXTENSION_MODEL_PROPERTY_KEY = "customExtensionModelProperty";
    public static final String CONFIGURATION_MODEL_PROPERTY_KEY = "customConfigurationModelProperty";
    public static final String OPERATION_MODEL_PROPERTY_KEY = "customOperationModelProperty";
    public static final String PARAMETER_MODEL_PROPERTY_KEY = "customParameterModelProperty";

    public static final String EXTENSION_MODEL_PROPERTY_VALUE = "customExtensionModelPropertyValue";
    public static final String CONFIGURATION_MODEL_PROPERTY_VALUE = "customConfigurationModelPropertyValue";
    public static final String OPERATION_MODEL_PROPERTY_VALUE = "customOperationModelPropertyValue";
    public static final String PARAMETER_MODEL_PROPERTY_VALUE = "customParameterModelPropertyValue";

    private final DeclarationDescriptor descriptor;
    private final ConfigurationInstantiator configurationInstantiator = mock(ConfigurationInstantiator.class);
    private final OperationExecutorFactory consumerExecutorFactory = mock(OperationExecutorFactory.class);
    private final OperationExecutorFactory broadcastExecutorFactory = mock(OperationExecutorFactory.class);
    private final OperationExecutorFactory argLessExecutorFactory = mock(OperationExecutorFactory.class);
    private final Interceptor interceptor1 = mock(Interceptor.class);
    private final Interceptor interceptor2 = mock(Interceptor.class);

    public TestWebServiceConsumerDeclarationReference()
    {
        descriptor = new DeclarationDescriptor();
        descriptor.named(WS_CONSUMER).describedAs(WS_CONSUMER_DESCRIPTION).onVersion(VERSION)
                .withModelProperty(EXTENSION_MODEL_PROPERTY_KEY, EXTENSION_MODEL_PROPERTY_VALUE)
                .withConfig(CONFIG_NAME).instantiatedWith(configurationInstantiator).describedAs(CONFIG_DESCRIPTION)
                    .withModelProperty(CONFIGURATION_MODEL_PROPERTY_KEY, CONFIGURATION_MODEL_PROPERTY_VALUE)
                    .withInterceptorFrom(() -> interceptor1)
                    .withInterceptorFrom(() -> interceptor2)
                    .with().requiredParameter(WSDL_LOCATION).describedAs(URI_TO_FIND_THE_WSDL).ofType(String.class).whichIsStatic().withModelProperty(PARAMETER_MODEL_PROPERTY_KEY, PARAMETER_MODEL_PROPERTY_VALUE)
                    .with().requiredParameter(SERVICE).describedAs(SERVICE_NAME).ofType(String.class)
                    .with().requiredParameter(PORT).describedAs(SERVICE_PORT).ofType(String.class)
                    .with().requiredParameter(ADDRESS).describedAs(SERVICE_ADDRESS).ofType(String.class)
                .withOperation(CONSUMER).describedAs(GO_GET_THEM_TIGER).executorsCreatedBy(consumerExecutorFactory)
                    .withModelProperty(OPERATION_MODEL_PROPERTY_KEY, OPERATION_MODEL_PROPERTY_VALUE)
                    .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(String.class)
                    .with().optionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                .withOperation(BROADCAST).describedAs(BROADCAST_DESCRIPTION).executorsCreatedBy(broadcastExecutorFactory)
                    .with().requiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(List.class, String.class)
                    .with().optionalParameter(MTOM_ENABLED).whichIsDynamic().describedAs(MTOM_DESCRIPTION).ofType(Boolean.class).defaultingTo(true)
                    .with().requiredParameter(CALLBACK).describedAs(CALLBACK_DESCRIPTION).whichIsStatic().ofType(OperationModel.class)
                .withOperation(ARG_LESS).describedAs(HAS_NO_ARGS).executorsCreatedBy(argLessExecutorFactory);
    }

    public DeclarationDescriptor getDescriptor()
    {
        return descriptor;
    }

    public ConfigurationInstantiator getConfigurationInstantiator()
    {
        return configurationInstantiator;
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

    public Interceptor getInterceptor1()
    {
        return interceptor1;
    }

    public Interceptor getInterceptor2()
    {
        return interceptor2;
    }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.tck.introspection;

import static org.mockito.Mockito.mock;
import static org.mule.runtime.extension.api.introspection.connection.ConnectionManagementType.NONE;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.REQUIRED;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.java.api.JavaTypeLoader;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.extension.api.Category;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.config.ConfigurationFactory;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderFactory;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ComponentDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConfigurationDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ConnectionProviderDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.runtime.operation.Interceptor;
import org.mule.runtime.extension.api.runtime.operation.OperationExecutorFactory;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * A simple pojo containing reference information for making test around a {@link ExtensionDeclarer}
 * which represents a theoretical &quot;Web Service Consumer&quot; extension.
 * <p>
 * It contains an actual {@link ExtensionDeclarer} that can be accessed through the {@link #getExtensionDeclarer()}
 * method plus some other getters which provides access to other declaration components
 * that you might want to make tests against.
 * <p>
 * This case focuses on the scenario in which all sources, providers and operations are available
 * on all configs
 *
 * @since 1.0
 */
public class TestWebServiceConsumerDeclarer {

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
  public static final MuleVersion MIN_MULE_VERSION = new MuleVersion("4.0");

  public static final int DEFAULT_PORT = 8080;

  public static final ModelProperty EXTENSION_MODEL_PROPERTY = new TestModelProperty("customExtensionModelProperty");
  public static final ModelProperty CONFIGURATION_MODEL_PROPERTY = new TestModelProperty("customConfigurationModelProperty");
  public static final ModelProperty OPERATION_MODEL_PROPERTY = new TestModelProperty("customOperationModelProperty");
  public static final ModelProperty PARAMETER_MODEL_PROPERTY = new TestModelProperty("customParameterModelProperty");
  public static final ModelProperty SOURCE_MODEL_PROPERTY = new TestModelProperty("customSourceModelProperty");

  public static final String CONNECTION_PROVIDER_NAME = "connectionProvider";
  public static final String CONNECTION_PROVIDER_DESCRIPTION = "my connection provider";
  public static final Class<?> CONNECTION_PROVIDER_CONNECTOR_TYPE = Integer.class;

  private final ExtensionDeclarer extensionDeclarer;
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
  private final Source<Object, Attributes> source = mock(Source.class);
  private final ConnectionProviderFactory connectionProviderFactory = mock(ConnectionProviderFactory.class);
  private final Optional<ExceptionEnricherFactory> exceptionEnricherFactory = Optional.of(mock(ExceptionEnricherFactory.class));
  private final BaseTypeBuilder<?> typeBuilder = BaseTypeBuilder.create(JavaTypeLoader.JAVA);
  private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  public TestWebServiceConsumerDeclarer() {
    extensionDeclarer = new ExtensionDeclarer();
    extensionDeclarer.named(WS_CONSUMER).describedAs(WS_CONSUMER_DESCRIPTION).onVersion(VERSION).fromVendor(MULESOFT)
        .withCategory(Category.SELECT).withMinMuleVersion(MIN_MULE_VERSION)
        .withExceptionEnricherFactory(exceptionEnricherFactory)
        .withModelProperty(EXTENSION_MODEL_PROPERTY);

    ConfigurationDeclarer config =
        extensionDeclarer.withConfig(CONFIG_NAME).createdWith(configurationFactory).describedAs(CONFIG_DESCRIPTION)
            .withModelProperty(CONFIGURATION_MODEL_PROPERTY)
            .withInterceptorFrom(() -> configInterceptor1)
            .withInterceptorFrom(() -> configInterceptor2);
    config.withRequiredParameter(ADDRESS).describedAs(SERVICE_ADDRESS).ofType(typeLoader.load(String.class));
    config.withRequiredParameter(PORT).describedAs(SERVICE_PORT).ofType(typeLoader.load(String.class));
    config.withRequiredParameter(SERVICE).describedAs(SERVICE_NAME).ofType(typeLoader.load(String.class));
    config.withRequiredParameter(WSDL_LOCATION).describedAs(URI_TO_FIND_THE_WSDL).ofType(typeLoader.load(String.class))
        .withExpressionSupport(NOT_SUPPORTED)
        .withModelProperty(PARAMETER_MODEL_PROPERTY);

    ComponentDeclarer operation =
        extensionDeclarer.withOperation(CONSUMER).describedAs(GO_GET_THEM_TIGER).executorsCreatedBy(consumerExecutorFactory)
            .withModelProperty(OPERATION_MODEL_PROPERTY);
    operation.withOutput().ofType(typeLoader.load(InputStream.class));
    operation.withOutputAttributes().ofType(typeLoader.load(String.class));

    operation.withRequiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(typeLoader.load(String.class));
    operation.withOptionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(typeLoader.load(Boolean.class))
        .defaultingTo(true);

    operation =
        extensionDeclarer.withOperation(BROADCAST).describedAs(BROADCAST_DESCRIPTION).executorsCreatedBy(broadcastExecutorFactory)
            .withExceptionEnricherFactory(exceptionEnricherFactory)
            .withInterceptorFrom(() -> operationInterceptor1)
            .withInterceptorFrom(() -> operationInterceptor2);

    operation.withOutput().ofType(typeLoader.load(void.class));
    operation.withRequiredParameter(OPERATION).describedAs(THE_OPERATION_TO_USE).ofType(typeBuilder.arrayType()
        .id(List.class.getName())
        .of(typeBuilder.stringType().id(String.class.getName()))
        .build());

    operation.withOptionalParameter(MTOM_ENABLED).describedAs(MTOM_DESCRIPTION).ofType(typeLoader.load(Boolean.class))
        .defaultingTo(true);
    operation.withRequiredParameter(CALLBACK).describedAs(CALLBACK_DESCRIPTION).ofType(typeLoader.load(OperationModel.class))
        .withExpressionSupport(REQUIRED);

    extensionDeclarer.withOperation(ARG_LESS).describedAs(HAS_NO_ARGS).executorsCreatedBy(argLessExecutorFactory).withOutput()
        .ofType(typeLoader.load(int.class));

    ConnectionProviderDeclarer connectionProvider =
        extensionDeclarer.withConnectionProvider(CONNECTION_PROVIDER_NAME).describedAs(CONNECTION_PROVIDER_DESCRIPTION)
            .createdWith(connectionProviderFactory)
            .whichGivesConnectionsOfType(CONNECTION_PROVIDER_CONNECTOR_TYPE)
            .withConnectionManagementType(NONE);

    connectionProvider.withRequiredParameter(USERNAME).describedAs(USERNAME_DESCRIPTION).ofType(typeLoader.load(String.class));
    connectionProvider.withRequiredParameter(PASSWORD).describedAs(PASSWORD_DESCRIPTION).ofType(typeLoader.load(String.class));

    ComponentDeclarer sourceDeclarer =
        extensionDeclarer.withMessageSource(LISTENER).describedAs(LISTEN_DESCRIPTION).sourceCreatedBy(() -> source)
            .withModelProperty(SOURCE_MODEL_PROPERTY)
            .withInterceptorFrom(() -> messageSourceInterceptor1)
            .withInterceptorFrom(() -> messageSourceInterceptor2);

    sourceDeclarer.withOutput().ofType(typeLoader.load(InputStream.class));
    sourceDeclarer.withOutputAttributes().ofType(typeLoader.load(Serializable.class));

    sourceDeclarer.withRequiredParameter(URL).describedAs(URL_DESCRIPTION).ofType(typeLoader.load(String.class));
    sourceDeclarer.withOptionalParameter(PORT).describedAs(PORT_DESCRIPTION).ofType(typeLoader.load(Integer.class))
        .defaultingTo(DEFAULT_PORT);
  }

  public ExtensionDeclarer getExtensionDeclarer() {
    return extensionDeclarer;
  }

  public ConfigurationFactory getConfigurationFactory() {
    return configurationFactory;
  }

  public OperationExecutorFactory getConsumerExecutorFactory() {
    return consumerExecutorFactory;
  }

  public OperationExecutorFactory getBroadcastExecutorFactory() {
    return broadcastExecutorFactory;
  }

  public OperationExecutorFactory getArgLessExecutorFactory() {
    return argLessExecutorFactory;
  }

  public Interceptor getConfigInterceptor1() {
    return configInterceptor1;
  }

  public Interceptor getConfigInterceptor2() {
    return configInterceptor2;
  }

  public Interceptor getOperationInterceptor1() {
    return operationInterceptor1;
  }

  public Interceptor getOperationInterceptor2() {
    return operationInterceptor2;
  }

  public ConnectionProviderFactory getConnectionProviderFactory() {
    return connectionProviderFactory;
  }

  public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory() {
    return exceptionEnricherFactory;
  }

  public Source getSource() {
    return source;
  }

  private static class TestModelProperty implements ModelProperty {

    private final String name;

    private TestModelProperty(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isExternalizable() {
      return true;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof TestModelProperty) {
        return name.equals(((TestModelProperty) obj).name);
      }

      return false;
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
}

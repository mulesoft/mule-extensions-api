/*
/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.config.ConfigurationFactory;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;

/**
 * Allows configuring a {@link ConfigurationDeclaration} through a fluent API
 *
 * @since 1.0
 */
public class ConfigurationDeclarer extends ParameterizedDeclarer<ConfigurationDeclaration> implements
    HasOperationDeclarer, HasConnectionProviderDeclarer, HasSourceDeclarer,
    HasModelProperties<ConfigurationDeclarer>, HasInterceptors<ConfigurationDeclarer> {

  /**
   * Creates a new instance
   *
   * @param declaration the declaration object to be configured
   */
  ConfigurationDeclarer(ConfigurationDeclaration declaration) {
    super(declaration);
  }

  /**
   * Adds a description to the configuration
   *
   * @param description a description
   * @return {@code this} declarer
   */
  public ConfigurationDeclarer describedAs(String description) {
    declaration.setDescription(description);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OperationDeclarer withOperation(String name) {
    OperationDeclaration operation = new OperationDeclaration(name);

    final OperationDeclarer operationDeclarer = new OperationDeclarer(operation);
    withOperation(operationDeclarer);

    return operationDeclarer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void withOperation(OperationDeclarer declarer) {
    declaration.addOperation(declarer.getDeclaration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectionProviderDeclarer withConnectionProvider(String name) {
    ConnectionProviderDeclaration declaration = new ConnectionProviderDeclaration(name);

    final ConnectionProviderDeclarer connectionProviderDeclarer = new ConnectionProviderDeclarer(declaration);
    withConnectionProvider(connectionProviderDeclarer);

    return connectionProviderDeclarer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void withConnectionProvider(ConnectionProviderDeclarer declarer) {
    declaration.addConnectionProvider(declarer.getDeclaration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SourceDeclarer withMessageSource(String name) {
    SourceDeclaration declaration = new SourceDeclaration(name);

    final SourceDeclarer sourceDeclarer = new SourceDeclarer(declaration);
    withMessageSource(sourceDeclarer);

    return sourceDeclarer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void withMessageSource(SourceDeclarer declarer) {
    declaration.addMessageSource(declarer.getDeclaration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigurationDeclarer withModelProperty(ModelProperty modelProperty) {
    declaration.addModelProperty(modelProperty);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigurationDeclarer withInterceptorFrom(InterceptorFactory interceptorFactory) {
    declaration.addInterceptorFactory(interceptorFactory);
    return this;
  }

  /**
   * Specifies the {@link ConfigurationFactory} to be used to
   * create instances of objects which are compliant with the declared
   * configuration
   *
   * @param configurationFactory a {@link ConfigurationFactory}
   * @return {@code this} declarer
   */
  public ConfigurationDeclarer createdWith(ConfigurationFactory configurationFactory) {
    declaration.setConfigurationFactory(configurationFactory);
    return this;
  }
}

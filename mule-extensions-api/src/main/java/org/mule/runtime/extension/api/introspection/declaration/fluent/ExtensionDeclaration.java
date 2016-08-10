/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.Category;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.exception.ExceptionEnricherFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A declaration object for a {@link ExtensionModel}. It contains raw, unvalidated
 * data which is used to declare the structure of a {@link ExtensionModel}
 *
 * @since 1.0
 */
public class ExtensionDeclaration extends NamedDeclaration<ExtensionDeclaration>
    implements ConnectedDeclaration<ExtensionDeclaration>, WithSourcesDeclaration<ExtensionDeclaration>,
    WithOperationsDeclaration<ExtensionDeclaration> {

  private final List<ConfigurationDeclaration> configurations = new LinkedList<>();
  private final SubDeclarationsContainer subDeclarations = new SubDeclarationsContainer();
  private String name;
  private String version;
  private String vendor;
  private Optional<ExceptionEnricherFactory> exceptionEnricherFactory;
  private Category category;
  private MuleVersion minMuleVersion;

  /**
   * Creates a new instance
   */
  ExtensionDeclaration() {
    super("");
  }

  /**
   * Returns an immutable list with the {@link ConfigurationDeclaration} instances
   * that have been declared so far.
   *
   * @return an unmodifiable list. May be empty but will never be {@code null}
   */
  public List<ConfigurationDeclaration> getConfigurations() {
    return configurations;
  }

  /**
   * Adds a {@link ConfigurationDeclaration}
   *
   * @param config a not {@code null} {@link ConfigurationDeclaration}
   * @return this declaration
   * @throws {@link IllegalArgumentException} if {@code config} is {@code null}
   */
  public ExtensionDeclaration addConfig(ConfigurationDeclaration config) {
    if (config == null) {
      throw new IllegalArgumentException("Can't add a null config");
    }

    configurations.add(config);
    return this;
  }

  /**
   * @return an unmodifiable {@link List} with
   * the available {@link OperationDeclaration}s
   */
  @Override
  public List<OperationDeclaration> getOperations() {
    return subDeclarations.getOperations();
  }

  /**
   * @return an unmodifiable {@link List} with the available {@link ConnectionProviderDeclaration}s
   */
  @Override
  public List<ConnectionProviderDeclaration> getConnectionProviders() {
    return subDeclarations.getConnectionProviders();
  }

  /**
   * @return an unmodifiable {@link List} with the available {@link SourceDeclaration}s
   */
  @Override
  public List<SourceDeclaration> getMessageSources() {
    return subDeclarations.getMessageSources();
  }

  /**
   * Adds a {@link ConnectionProviderDeclaration}
   *
   * @param connectionProvider a not {@code null} {@link ConnectionProviderDeclaration}
   * @return {@code this} declaration
   * @throws IllegalArgumentException if {@code connectionProvider} is {@code null}
   */
  @Override
  public ExtensionDeclaration addConnectionProvider(ConnectionProviderDeclaration connectionProvider) {
    subDeclarations.addConnectionProvider(connectionProvider);
    return this;
  }

  /**
   * Adds a {@link OperationDeclaration}
   *
   * @param operation a not {@code null} {@link OperationDeclaration}
   * @return {@code this} declaration
   * @throws {@link IllegalArgumentException} if {@code operation} is {@code null}
   */
  @Override
  public ExtensionDeclaration addOperation(OperationDeclaration operation) {
    subDeclarations.addOperation(operation);
    return this;
  }

  /**
   * Adds a {@link SourceDeclaration}
   *
   * @param sourceDeclaration a not {@code null} {@link SourceDeclaration}
   * @return {@code this} declaration
   * @throws {@link IllegalArgumentException} if {@code sourceDeclaration} is {@code null}
   */
  @Override
  public ExtensionDeclaration addMessageSource(SourceDeclaration sourceDeclaration) {
    subDeclarations.addMessageSource(sourceDeclaration);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  void setVersion(String version) {
    this.version = version;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public Optional<ExceptionEnricherFactory> getExceptionEnricherFactory() {
    return exceptionEnricherFactory;
  }

  public void setExceptionEnricherFactory(Optional<ExceptionEnricherFactory> exceptionEnricherFactory) {
    this.exceptionEnricherFactory = exceptionEnricherFactory;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return this.category;
  }

  public void setMinMuleVersion(MuleVersion minMuleVersion) {
    this.minMuleVersion = minMuleVersion;
  }

  public MuleVersion getMinMuleVersion() {
    return minMuleVersion;
  }
}

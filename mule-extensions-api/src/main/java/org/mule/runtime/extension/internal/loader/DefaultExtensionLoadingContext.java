/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;
import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.mule.runtime.extension.api.loader.ExtensionModelLoadingRequest.builder;
import static org.mule.runtime.extension.internal.ocs.PlatformManagedOAuthUtils.isPlatformManagedOAuthEnabled;

import org.mule.runtime.api.artifact.ArtifactCoordinates;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionModelLoadingRequest;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of {@link ExtensionLoadingContext}. The fact that this class's attributes are immutable, doesn't mean
 * that their inner state is in fact immutable also.
 *
 * @since 1.0
 */
public final class DefaultExtensionLoadingContext implements ExtensionLoadingContext {

  private final ExtensionModelLoadingRequest request;
  private final ExtensionDeclarer extensionDeclarer;
  private final List<ExtensionModelValidator> customValidators = new LinkedList<>();
  private final List<DeclarationEnricher> customDeclarationEnrichers = new LinkedList<>();
  private final Map<String, Object> customParameters;

  public DefaultExtensionLoadingContext(ClassLoader extensionClassLoader, DslResolvingContext dslResolvingContext) {
    this(new ExtensionDeclarer(), builder(extensionClassLoader, dslResolvingContext).build());
  }

  public DefaultExtensionLoadingContext(ExtensionDeclarer declarer,
                                        ClassLoader extensionClassLoader,
                                        DslResolvingContext dslResolvingContext) {
    this(declarer, builder(extensionClassLoader, dslResolvingContext).build());
  }

  public DefaultExtensionLoadingContext(ExtensionModelLoadingRequest request) {
    this(new ExtensionDeclarer(), request);
  }

  public DefaultExtensionLoadingContext(ExtensionDeclarer extensionDeclarer, ExtensionModelLoadingRequest request) {
    checkArgument(extensionDeclarer != null, "extension declarer cannot be null");
    checkArgument(request != null, "request cannot be null");

    this.extensionDeclarer = extensionDeclarer;
    customParameters = new HashMap<>(request.getParameters());
    this.request = request;
  }

  /**
   * {@inheritDoc}
   */
  public ExtensionDeclarer getExtensionDeclarer() {
    return extensionDeclarer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addParameter(String key, Object value) {
    checkArgument(key != null && key.length() > 0, "key cannot be blank");
    checkArgument(value != null, "value cannot be null");

    customParameters.put(key, value);
  }

  @Override
  public void addParameters(Map<String, Object> parameters) {
    checkArgument(parameters != null, "cannot add null parameters");
    customParameters.putAll(parameters);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Optional<T> getParameter(String key) {
    return ofNullable((T) customParameters.get(key));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionLoadingContext addCustomValidator(ExtensionModelValidator extensionModelValidator) {
    checkArgument(extensionModelValidator != null, "custom validator cannot be null");
    customValidators.add(extensionModelValidator);

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionLoadingContext addCustomValidators(Collection<ExtensionModelValidator> extensionModelValidators) {
    checkArgument(extensionModelValidators != null, "custom validators cannot be null");
    customValidators.addAll(extensionModelValidators);

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionLoadingContext addCustomDeclarationEnricher(DeclarationEnricher enricher) {
    checkArgument(enricher != null, "custom enricher cannot be null");
    customDeclarationEnrichers.add(enricher);

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionLoadingContext addCustomDeclarationEnrichers(Collection<DeclarationEnricher> enrichers) {
    checkArgument(enrichers != null, "custom enrichers cannot be null");
    customDeclarationEnrichers.addAll(enrichers);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassLoader getExtensionClassLoader() {
    return request.getExtensionClassLoader();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DslResolvingContext getDslResolvingContext() {
    return request.getDslResolvingContext();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOCSEnabled() {
    return request.isOCSEnabled() || isPlatformManagedOAuthEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ArtifactCoordinates> getArtifactCoordinates() {
    return ofNullable(request.getArtifactCoordinates());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DeclarationEnricher> getCustomDeclarationEnrichers() {
    List<DeclarationEnricher> enrichers = new ArrayList<>(customDeclarationEnrichers.size() + request.getEnrichers().size());
    enrichers.addAll(customDeclarationEnrichers);
    enrichers.addAll(request.getEnrichers());

    return unmodifiableList(enrichers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ExtensionModelValidator> getCustomValidators() {
    List<ExtensionModelValidator> validators = new ArrayList<>(customValidators.size() + request.getValidators().size());
    validators.addAll(customValidators);
    validators.addAll(request.getValidators());

    return unmodifiableList(validators);
  }

}

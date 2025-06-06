/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.api.annotation.NoImplement;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.api.artifact.ArtifactCoordinates;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.version.HasMinMuleVersion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Used for propagating state across all the components that take part into loading an {@link ExtensionModel}.
 * <p>
 * Each instance should be used to load one and only one {@link ExtensionModel}. Use different instances if you're going to load
 * several models.
 *
 * @since 1.0
 */
@NoImplement
public interface ExtensionLoadingContext {

  final String EXTENSION_LOADER_PROPERTY_PREFIX = "EXTENSION_LOADER_";

  /**
   * The {@link ExtensionDeclarer} in which the extension is being described into
   *
   * @return a non {@code null} {@link ExtensionDeclarer}
   */
  ExtensionDeclarer getExtensionDeclarer();

  /**
   * @return a type loader for the types from Java classes within an extension.
   *
   * @since 1.9
   */
  ClassTypeLoader getTypeLoader();

  /**
   * Adds a custom parameter registered under {@code key}
   *
   * @param key   the key under which the {@code value} is to be registered
   * @param value the custom parameter value
   * @throws IllegalArgumentException if {@code key} or {@code value} are {@code null}
   */
  void addParameter(String key, Object value);

  /**
   * Adds the contents of the given map as custom parameters
   *
   * @param parameters a map with custom parameters
   */
  void addParameters(Map<String, Object> parameters);

  /**
   * Obtains the custom parameter registered under {@code key}.
   *
   * @param key the key under which the wanted value is registered
   * @param <T> generic type of the expected value
   * @return an {@link Optional} value
   */
  <T> Optional<T> getParameter(String key);

  /**
   * Registers a custom {@link ExtensionModelValidator} to be executed on top of the ones which the runtime applies by default.
   * <p>
   * Custom validators will not apply globally but just for the model being loaded with this context.
   *
   * @param extensionModelValidator the custom validator
   * @return {@code this} instance
   */
  ExtensionLoadingContext addCustomValidator(ExtensionModelValidator extensionModelValidator);

  /**
   * Registers custom {@link ExtensionModelValidator} to be executed on top of the ones which the runtime applies by default.
   * <p>
   * These custom validators will not apply globaly but just for the model being loaded with this context.
   *
   * @param extensionModelValidators the custom validators
   * @return {@code this} instance
   */
  ExtensionLoadingContext addCustomValidators(Collection<ExtensionModelValidator> extensionModelValidators);

  /**
   * Registers a custom {@link DeclarationEnricher} which is executed <b>before</b> the ones that the runtime automatically
   * applies.
   *
   * @param enricher the custom enricher
   * @return {@code this} instance
   */
  ExtensionLoadingContext addCustomDeclarationEnricher(DeclarationEnricher enricher);

  /**
   * Registers custom {@link DeclarationEnricher} which are executed <b>before</b> the ones that the runtime automatically
   * applies.
   *
   * @param enrichers the custom enrichers
   * @return {@code this} instance
   */
  ExtensionLoadingContext addCustomDeclarationEnrichers(Collection<DeclarationEnricher> enrichers);

  /**
   * @return an immutable list with the registered custom enrichers
   */
  List<DeclarationEnricher> getCustomDeclarationEnrichers();

  /**
   * @return an immutable list with the registered custom validators
   */
  List<ExtensionModelValidator> getCustomValidators();

  /**
   * @return the extension's {@link ClassLoader}
   */
  ClassLoader getExtensionClassLoader();

  /**
   * @return the {@link DslResolvingContext} with all the dependencies to load an {@link ExtensionModel}
   */
  DslResolvingContext getDslResolvingContext();

  /**
   * @return whether OCS is enabled
   * @since 1.5
   */
  boolean isOCSEnabled();

  /**
   * @return whether the extension must be validated after being loaded.
   * @since 1.7
   */
  boolean isForceExtensionValidation();

  /**
   * @return whether the {@link HasMinMuleVersion#getMinMuleVersion() minMuleVersion} of each component must be calculated.
   * @since 1.9
   */
  boolean isResolveMinMuleVersion();

  /**
   * @return the {@link ArtifactCoordinates} of the Extension
   * @since 1.5
   */
  default Optional<ArtifactCoordinates> getArtifactCoordinates() {
    return Optional.empty();
  }
}

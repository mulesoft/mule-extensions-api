/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.mule.runtime.api.util.Preconditions.checkArgument;

import org.mule.runtime.api.artifact.ArtifactCoordinates;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Parameterizes the loading of one specific {@link ExtensionModel}
 *
 * @since 1.5.0
 */
public final class ExtensionModelLoadingRequest {

  /**
   * Builder for creating a new {@link ExtensionModelLoadingRequest}
   */
  public static final class Builder {

    private final ExtensionModelLoadingRequest product;

    private Builder(ClassLoader extensionClassLoader, DslResolvingContext dslResolvingContext) {
      this.product = new ExtensionModelLoadingRequest(extensionClassLoader, dslResolvingContext);
    }

    /**
     * Adds a custom parameter registered under {@code key}
     *
     * @param key   the key under which the {@code value} is to be registered
     * @param value the custom parameter value
     * @return {@code this} builder
     * @throws IllegalArgumentException if {@code key} or {@code value} are {@code null}
     */
    public Builder addParameter(String key, Object value) {
      checkArgument(key != null && key.length() > 0, "key cannot be blank");
      checkArgument(value != null, "value cannot be null");

      product.parameters.put(key, value);
      return this;
    }

    /**
     * Adds the contents of the given map as custom parameters
     *
     * @param parameters a map with custom parameters
     * @return {@code this} builder
     * @throws IllegalArgumentException if any of the map entries contains a {@code null} value
     */
    public Builder addParameters(Map<String, Object> parameters) {
      parameters.forEach((k, v) -> addParameter(k, v));
      return this;
    }

    /**
     * Registers an {@link ExtensionModelValidator} added to the ones applied by default.
     *
     * @param validator the added validator
     * @return {@code this} builder
     * @throws IllegalArgumentException if the validator is {@code null}
     */
    public Builder addValidator(ExtensionModelValidator validator) {
      checkArgument(validator != null, "validator cannot be null");
      product.validators.add(validator);

      return this;
    }

    /**
     * Registers a {@link DeclarationEnricher} added to the ones applied by default
     *
     * @param enricher the added enricher
     * @return {@code this} builder
     * @throws IllegalArgumentException if the enricher is {@code null}
     */
    public Builder addEnricher(DeclarationEnricher enricher) {
      checkArgument(enricher != null, "enricher cannot be null");
      product.enrichers.add(enricher);

      return this;
    }

    /**
     * Registers a {@link ArtifactCoordinates}
     *
     * @param artifactCoordinates to be set
     * @return {@code this} builder
     * @throws IllegalArgumentException if the artifactCoordinates is {@code null}
     */
    public Builder setArtifactCoordinates(ArtifactCoordinates artifactCoordinates) {
      checkArgument(artifactCoordinates != null, "artifactCoordinates cannot be null");
      product.artifactCoordinates = artifactCoordinates;

      return this;
    }

    /**
     * Enables or disables OCS.
     *
     * @param ocsEnabled whether OCS is enabled
     * @return {@code this} builder
     */
    public Builder setOCSEnabled(boolean ocsEnabled) {
      product.ocsEnabled = ocsEnabled;

      return this;
    }

    /**
     * @return The built request
     */
    public ExtensionModelLoadingRequest build() {
      return product;
    }
  }

  /**
   * @param extensionClassLoader The extension's {@link ClassLoader}
   * @param dslResolvingContext  a {@link DslResolvingContext}
   * @return a new {@link Builder}
   */
  public static Builder builder(ClassLoader extensionClassLoader, DslResolvingContext dslResolvingContext) {
    return new Builder(extensionClassLoader, dslResolvingContext);
  }

  private final ClassLoader extensionClassLoader;
  private final DslResolvingContext dslResolvingContext;
  private final List<ExtensionModelValidator> validators = new LinkedList<>();
  private final List<DeclarationEnricher> enrichers = new LinkedList<>();
  private final Map<String, Object> parameters = new HashMap<>();
  private ArtifactCoordinates artifactCoordinates;
  private boolean ocsEnabled;

  private ExtensionModelLoadingRequest(ClassLoader extensionClassLoader, DslResolvingContext dslResolvingContext) {
    checkArgument(extensionClassLoader != null, "extension classLoader cannot be null");
    checkArgument(dslResolvingContext != null, "Dsl resolving context cannot be null");

    this.extensionClassLoader = extensionClassLoader;
    this.dslResolvingContext = dslResolvingContext;
  }

  /**
   * @return The extension's classloader
   */
  public ClassLoader getExtensionClassLoader() {
    return extensionClassLoader;
  }

  /**
   * @return the acting {@link DslResolvingContext}
   */
  public DslResolvingContext getDslResolvingContext() {
    return dslResolvingContext;
  }

  /**
   * @return whether OCS is enabled
   */
  public boolean isOCSEnabled() {
    return ocsEnabled;
  }

  /**
   * @return an unmodifiable list of custom validators added to the ones applied by default.
   */
  public List<ExtensionModelValidator> getValidators() {
    return unmodifiableList(validators);
  }

  /**
   * @return an unmodifiable list of custom enrichers added to the ones applied by default.
   */
  public List<DeclarationEnricher> getEnrichers() {
    return unmodifiableList(enrichers);
  }

  /**
   * @return parameters for this loading request
   */
  public Map<String, Object> getParameters() {
    return unmodifiableMap(parameters);
  }

  /**
   * @return the {@link ArtifactCoordinates} of the Extension
   */
  public ArtifactCoordinates getArtifactCoordinates() {
    return artifactCoordinates;
  }
}

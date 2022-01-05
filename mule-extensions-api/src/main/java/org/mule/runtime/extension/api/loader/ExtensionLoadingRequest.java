/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import static org.mule.runtime.api.util.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ExtensionLoadingRequest {

  public static final class Builder {

    private final ExtensionLoadingRequest product = new ExtensionLoadingRequest();

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
     * Registers an {@link ExtensionModelValidator} executed after the ones which the runtime applies by default.
     * <p>
     * This validator will not apply globally but just for the model being loaded with this request.
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
     * Registers a {@link DeclarationEnricher} which is executed <b>before</b> the ones that the runtime automatically
     * applies.
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

    public ExtensionLoadingRequest build() {
      return product;
    }

  }

  private final List<ExtensionModelValidator> validators = new LinkedList<>();
  private final List<DeclarationEnricher> enrichers = new LinkedList<>();
  private final Map<String, Object> parameters = new HashMap<>();

  private ExtensionLoadingRequest() {
  }

  public List<ExtensionModelValidator> getValidators() {
    return validators;
  }

  public List<DeclarationEnricher> getEnrichers() {
    return enrichers;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }
}

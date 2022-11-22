/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.acceptsExpressions;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addExpirationPolicy;

import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.runtime.ExpirationPolicy;

/**
 * Adds an {@link ExpirationPolicy} parameter to all configs which might be used in a dynamic way
 *
 * @since 1.0
 */
public class DynamicConfigDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    extensionLoadingContext.getExtensionDeclarer().getDeclaration().getConfigurations().forEach(config -> {
      if (canBeDynamic(config)) {
        addExpirationPolicy(config);
      }
    });
  }

  private boolean canBeDynamic(ConfigurationDeclaration config) {
    if (hasAnyDynamic(config)) {
      return true;
    }

    return config.getConnectionProviders().stream().anyMatch(this::hasAnyDynamic);
  }

  private boolean hasAnyDynamic(ParameterizedDeclaration<?> declaration) {
    return declaration.getAllParameters().stream().anyMatch(p -> acceptsExpressions(p.getExpressionSupport()));
  }
}

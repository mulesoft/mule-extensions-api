/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.INITIALIZE;

import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.ClassLoaderModelProperty;

/**
 * Adds a {@link ClassLoaderModelProperty} pointing to {@link ExtensionLoadingContext#getExtensionClassLoader()}
 *
 * @since 1.0
 */
public class ClassLoaderDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return INITIALIZE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    ClassLoader classLoader = extensionLoadingContext.getExtensionClassLoader();
    if (classLoader == null) {
      throw new IllegalModelDefinitionException("No ClassLoader was specified for extension " +
          extensionLoadingContext.getExtensionDeclarer().getDeclaration().getName());
    }

    extensionLoadingContext.getExtensionDeclarer().withModelProperty(new ClassLoaderModelProperty(classLoader));
  }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;

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
    return POST_STRUCTURE;
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

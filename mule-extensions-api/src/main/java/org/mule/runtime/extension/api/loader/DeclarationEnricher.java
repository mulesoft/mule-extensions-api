/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE_DECLARATION;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.util.Collection;


/**
 * Enriches {@link ExtensionDeclarer declarers} before they are actually turned
 * into models.
 *
 * The runtime contains a set of default instances which should always apply to any model,
 * but each {@link ExtensionModelLoader} can specify their own through the
 * {@link ExtensionLoadingContext#addCustomDeclarationEnrichers(Collection)}
 * method.
 *
 * @since 1.0
 */
public interface DeclarationEnricher {


  default DeclarationEnricherPhase getExecutionPhase() {
    return POST_STRUCTURE_DECLARATION;
  }

  /**
   * Enriches the descriptor provided in the given {@code extensionLoadingContext}.
   *
   * @param extensionLoadingContext a {@link ExtensionLoadingContext}
   */
  void enrich(ExtensionLoadingContext extensionLoadingContext);

}

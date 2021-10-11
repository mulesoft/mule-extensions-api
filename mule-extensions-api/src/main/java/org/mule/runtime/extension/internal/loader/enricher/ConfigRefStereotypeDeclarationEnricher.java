/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.WIRING;
import static org.mule.runtime.internal.dsl.DslConstants.CONFIG_ATTRIBUTE_NAME;

import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

public class ConfigRefStereotypeDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return WIRING;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new DeclarationWalker() {

      @Override
      protected void onConfiguration(ConfigurationDeclaration config) {
        final StereotypeModel stereotype = config.getStereotype();
        if (stereotype != null) {
          config.getConstructs().forEach(construct -> addConfigRefStereotype(construct, stereotype));
          config.getMessageSources().forEach(source -> addConfigRefStereotype(source, stereotype));
          config.getOperations().forEach(operation -> addConfigRefStereotype(operation, stereotype));
        }
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void addConfigRefStereotype(ComponentDeclaration<?> declaration, StereotypeModel stereotype) {
    declaration.getAllParameters().stream()
        .filter(p -> CONFIG_ATTRIBUTE_NAME.equals(p.getName()))
        .findAny()
        .ifPresent(configRef -> configRef.getAllowedStereotypeModels().add(stereotype));
  }
}

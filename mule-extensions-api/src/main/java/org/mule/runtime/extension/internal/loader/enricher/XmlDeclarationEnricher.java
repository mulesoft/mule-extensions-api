/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.empty;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.INITIALIZE;
import static org.mule.runtime.extension.api.util.XmlModelUtils.createXmlLanguageModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

public final class XmlDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return INITIALIZE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclarer extensionDeclarer = extensionLoadingContext.getExtensionDeclarer();
    ExtensionDeclaration extensionDeclaration = extensionDeclarer.getDeclaration();
    if (extensionDeclaration.getXmlDslModel() == null) {
      extensionDeclarer.withXmlDsl(createXmlLanguageModel(empty(), empty(), extensionDeclaration.getName(),
                                                          extensionDeclaration.getVersion()));
    }
  }
}

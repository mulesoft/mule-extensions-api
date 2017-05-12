/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import java.util.List;

/**
 * Sets defaults and other configurations on content parameters so that they
 * provider a consistent experience.
 * <p>
 * For example:
 * <p>
 * <ul>
 * <li>Primary content parameters are set as optional and defaulting to {@link Optional#PAYLOAD}</li>
 * <li>If a component only has one content parameter, then it's assigned the {@link ParameterRole#PRIMARY_CONTENT} role</li>
 * </ul>
 * <p>
 * This all happens for sources and operations. Connection providers and configs are not considered
 *
 * @since 1.0
 */
public final class ContentParameterDeclarationEnricher implements DeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        doEnrich(declaration);
      }

      @Override
      protected void onSource(SourceDeclaration declaration) {
        doEnrich(declaration);
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void doEnrich(ParameterizedDeclaration declaration) {
    List<ParameterDeclaration> contentParameters = getContentParameters(declaration.getAllParameters());
    if (contentParameters.isEmpty()) {
      return;
    }

    if (contentParameters.size() == 1) {
      contentParameters.get(0).setParameterRole(PRIMARY_CONTENT);
    }

    contentParameters.forEach(p -> {
      configureDsl(p);
      if (p.getRole() == PRIMARY_CONTENT) {
        if (p.getDefaultValue() == null) {
          p.setDefaultValue(PAYLOAD);
        }
        p.setRequired(false);
      }
    });
  }

  private void configureDsl(ParameterDeclaration p) {
    p.setDslConfiguration(ParameterDslConfiguration.builder(p.getDslConfiguration())
        .allowsReferences(false)
        .allowsInlineDefinition(true)
        .build());
  }

  private List<ParameterDeclaration> getContentParameters(List<ParameterDeclaration> parameters) {
    return parameters.stream().filter(p -> p.getRole() != BEHAVIOUR).collect(toList());
  }
}

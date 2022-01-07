/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;

import org.mule.metadata.api.model.BooleanType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

/**
 * {@link DeclarationEnricher} implementation that walks through all the {@link BooleanType} parameters and sets them as optional.
 * It also enriches those parameters with a default value of "false" if they don't have one.
 * <p>
 * Parameters acting as a config override or with {@link org.mule.runtime.api.meta.ExpressionSupport#REQUIRED} expression support
 * are skipped.
 *
 * @since 1.5.0
 */
public class BooleanParameterDeclarationEnricher implements DeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return POST_STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    new DeclarationWalker() {

      @Override
      protected void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                                 ParameterDeclaration declaration) {
        declaration.getType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitBoolean(BooleanType booleanType) {
            if (declaration.getExpressionSupport() == REQUIRED) {
              return;
            }

            declaration.setRequired(false);
            if (declaration.getDefaultValue() == null && !declaration.isConfigOverride()) {
              declaration.setDefaultValue(valueOf(FALSE));
            }
          }
        });
      }
    }.walk(extensionDeclaration);
  }
}


/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;
import static org.mule.runtime.extension.api.loader.ExtensionLoadingContext.EXTENSION_LOADER_PROPERTY_PREFIX;

import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import org.mule.metadata.api.model.BooleanType;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;

import java.util.Optional;

/**
 * {@link DeclarationEnricher} implementation that walks through all the {@link BooleanType} parameters and sets them as optional.
 * It also enriches those parameters with a default value of "false" if they don't have one.
 * <p>
 * Parameters acting as a config override or with {@link org.mule.runtime.api.meta.ExpressionSupport#REQUIRED} expression support
 * are skipped.
 *
 * @since 1.5.0
 */
public class BooleanParameterDeclarationEnricher implements WalkingDeclarationEnricher {

  /**
   * Avoids setting a default value of {@code false} to parameters of boolean type.
   * <p>
   * This is used to avoid W-12003688.
   *
   * @since 1.5.0
   */
  public static final String DONT_SET_DEFAULT_VALUE_TO_BOOLEAN_PARAMS =
      EXTENSION_LOADER_PROPERTY_PREFIX + "DONT_SET_DEFAULT_VALUE_TO_BOOLEAN_PARAMS";

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return POST_STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    if ((boolean) extensionLoadingContext.getParameter(DONT_SET_DEFAULT_VALUE_TO_BOOLEAN_PARAMS)
        .filter(v -> (v instanceof Boolean))
        .orElse(false)) {
      return empty();
    }

    return of(new DeclarationEnricherWalkDelegate() {

      @Override
      public void onParameter(ParameterizedDeclaration owner,
                              ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {

        if (declaration.getType() instanceof BooleanType) {
          if (declaration.getExpressionSupport() == REQUIRED) {
            return;
          }

          declaration.setRequired(false);
          if (declaration.getDefaultValue() == null && !declaration.isConfigOverride()) {
            declaration.setDefaultValue(valueOf(FALSE));
          }
        }
      }
    });
  }
}


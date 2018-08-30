/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.declaration.fluent.*;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.NAME_PARAM_DESCRIPTION;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;

/**
 * Enriches constructs models with the synthetic "name" parameter.
 * <p>
 * This name parameter is the Component ID of the construct meaning that the value associated to it can be used to reference the
 * construct in a mule application uniquely across all the instances of the same.
 *
 * @since 1.2
 */
public class NamedObjectDeclarationEnricher implements DeclarationEnricher {

  private static final MetadataType STRING_TYPE = BaseTypeBuilder.create(JAVA).stringType().build();

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onConfiguration(ConfigurationDeclaration declaration) {
        declaration.getDefaultParameterGroup().addParameter(buildNameParameter());
      }

    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private ParameterDeclaration buildNameParameter() {
    ParameterDeclaration nameParameter = new ParameterDeclaration("name");
    nameParameter.setExpressionSupport(NOT_SUPPORTED);
    nameParameter.setParameterRole(BEHAVIOUR);
    nameParameter.setDescription(NAME_PARAM_DESCRIPTION);
    nameParameter.setType(STRING_TYPE, false);
    nameParameter.setRequired(true);
    nameParameter.setComponentId(true);
    return nameParameter;
  }

}

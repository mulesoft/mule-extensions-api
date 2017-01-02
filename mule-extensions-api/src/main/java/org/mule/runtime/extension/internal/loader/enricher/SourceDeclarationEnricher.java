/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_REDELIVERY_POLICY_QNAME;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.internal.property.QNameModelProperty;

/**
 * An {@link InfrastructureDeclarationEnricher} which adds the following to all {@link SourceDeclaration}:
 *
 * <ul>
 *   <li>A Redelivery policy parameter</li>
 *   <li>A reconnection strategy parameter</li>
 * </ul>
 *
 * @since 1.0
 */
public final class SourceDeclarationEnricher extends InfrastructureDeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onSource(SourceDeclaration declaration) {
        addRedeliveryPolicy(declaration);
        addReconnectionStrategyParameter(declaration);
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  protected void addRedeliveryPolicy(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(REDELIVERY_POLICY_PARAMETER_NAME);
    parameter.setDescription(REDELIVERY_POLICY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new RedeliveryPolicyTypeBuilder().buildRedeliveryPolicyType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslModel(ElementDslModel.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ABSTRACT_REDELIVERY_POLICY_QNAME));
    parameter.addModelProperty(new InfrastructureParameterModelProperty());
    markAsInfrastructure(parameter);

    declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);
  }
}

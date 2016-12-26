/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.CONNECTION;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.internal.property.QNameModelProperty;

/**
 * Base class for {@link DeclarationEnricher} which add infrastructure parameters
 *
 * @since 1.0
 */
abstract class InfrastructureDeclarationEnricher implements DeclarationEnricher {

  protected void addReconnectionStrategyParameter(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(RECONNECTION_STRATEGY_PARAMETER_NAME);
    parameter.setDescription(RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new ReconnectionStrategyTypeBuilder().builReconnectionStrategyType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME));
    markAsInfrastructure(parameter);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }

  protected void markAsInfrastructure(ParameterDeclaration parameter) {
    parameter.addModelProperty(new InfrastructureParameterModelProperty());
  }
}

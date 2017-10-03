/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.CONNECTION;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_TAB_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_DEFAULT_RECONNECTION_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_REDELIVERY_POLICY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_EXPIRATION_POLICY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_POOLING_PROFILE_TYPE_QNAME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.type.DynamicConfigExpirationTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.PoolingProfileTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.api.property.QNameModelProperty;

import javax.xml.namespace.QName;

/**
 * Utility builder for all the infrastructure parameters
 *
 * @since 1.0
 */
public final class InfrastructureParameterBuilder {

  private InfrastructureParameterBuilder() {}

  public static void addReconnectionConfigParameter(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(RECONNECTION_CONFIG_PARAMETER_NAME);
    parameter.setDescription(RECONNECTION_CONFIG_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new ReconnectionStrategyTypeBuilder().buildReconnectionConfigType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ABSTRACT_DEFAULT_RECONNECTION_QNAME));
    markAsInfrastructure(parameter, 3);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }


  public static void addReconnectionStrategyParameter(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(RECONNECTION_STRATEGY_PARAMETER_NAME);
    parameter.setDescription(RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new ReconnectionStrategyTypeBuilder().buildReconnectionStrategyType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME));
    markAsInfrastructure(parameter, 3);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }

  public static void addPoolingProfileParameter(ConnectionProviderDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(POOLING_PROFILE_PARAMETER_NAME);
    parameter.setDescription(POOLING_PROFILE_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new PoolingProfileTypeBuilder().buildPoolingProfileType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_POOLING_PROFILE_TYPE_QNAME));
    markAsInfrastructure(parameter, 5);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }

  public static void addRedeliveryPolicy(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(REDELIVERY_POLICY_PARAMETER_NAME);
    parameter.setDescription(REDELIVERY_POLICY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new RedeliveryPolicyTypeBuilder().buildRedeliveryPolicyType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(REDELIVERY_TAB_NAME).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ABSTRACT_REDELIVERY_POLICY_QNAME));
    markAsInfrastructure(parameter, 1);

    declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);
  }

  public static ParameterDeclaration addStreamingParameter(ComponentDeclaration declaration,
                                                           MetadataType type,
                                                           QName qName) {
    ParameterDeclaration parameter = new ParameterDeclaration(STREAMING_STRATEGY_PARAMETER_NAME);
    parameter.setDescription(STREAMING_STRATEGY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(type, false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(qName));
    markAsInfrastructure(parameter, 2);

    declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);

    return parameter;
  }

  public static ParameterDeclaration addExpirationPolicy(ConfigurationDeclaration config) {
    ParameterDeclaration parameter = new ParameterDeclaration(EXPIRATION_POLICY_PARAMETER_NAME);
    parameter.setDescription(EXPIRATION_POLICY_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new DynamicConfigExpirationTypeBuilder().buildExpirationPolicyType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());

    parameter.addModelProperty(new QNameModelProperty(MULE_EXPIRATION_POLICY_QNAME));
    markAsInfrastructure(parameter, 4);

    config.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);

    return parameter;
  }

  public static void markAsInfrastructure(ParameterDeclaration parameter, int sequence) {
    parameter.addModelProperty(new InfrastructureParameterModelProperty(sequence));
  }
}

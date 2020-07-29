/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.CONNECTION;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.ERROR_MAPPINGS;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.ERROR_MAPPINGS_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.ERROR_MAPPINGS_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.PRIMARY_NODE_ONLY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.PRIMARY_NODE_ONLY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_TAB_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ERROR_MAPPING_TAB;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_DEFAULT_RECONNECTION_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_REDELIVERY_POLICY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ERROR_MAPPING_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_EXPIRATION_POLICY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_POOLING_PROFILE_TYPE_QNAME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.UnionType;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.type.DynamicConfigExpirationTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.ErrorMappingsTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.PoolingProfileTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.api.property.QNameModelProperty;
import org.mule.runtime.extension.api.property.SinceMuleVersionModelProperty;

import java.util.NoSuchElementException;

import javax.xml.namespace.QName;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * Utility builder for all the infrastructure parameters
 *
 * @since 1.0
 */
public final class InfrastructureParameterBuilder {

  private InfrastructureParameterBuilder() {}

  private static final String RECONNECTION_CONFIG_TYPE_KEY = "reconnectionConfig";
  private static final String RECONNECTION_STRATEGY_TYPE_KEY = "reconnectionStrategy";
  private static final String POOLING_PROFILE_TYPE_KEY = "poolingProfile";
  public static final String REDELIVERY_POLICY_TYPE_KEY = "redeliveryPolicy";
  private static final String DYNAMIC_EXPIRATION_TYPE_KEY = "expirationPolicy";
  private static final String ERROR_MAPPINGS_TYPE_KEY = "errorMappings";

  public static final String RECONNECT_SIMPLE_TYPE_KEY = "reconnect";
  public static final String RECONNECT_FOREVER_TYPE_KEY = "reconnectForever";

  public static final String STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_STREAM = "repeatableInMemoryStream";
  public static final String STREAMING_STRATEGY_REPEATABLE_FILE_STREAM = "repeatableFileStoreStream";
  public static final String STREAMING_STRATEGY_NON_REPEATABLE_STREAM = "nonRepeatableStream";
  public static final String STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_ITERABLE = "repeatableInMemoryIterable";
  public static final String STREAMING_STRATEGY_REPEATABLE_FILE_ITERABLE = "repeatableFileStoreIterable";
  public static final String STREAMING_STRATEGY_NON_REPEATABLE_ITERABLE = "nonRepeatableIterable";

  private static final LoadingCache<String, MetadataType> METADATA_TYPES_CACHE =
      newBuilder().weakValues().build(key -> {
        switch (key) {
          case RECONNECTION_CONFIG_TYPE_KEY:
            return new ReconnectionStrategyTypeBuilder().buildReconnectionConfigType();
          case RECONNECTION_STRATEGY_TYPE_KEY:
            return new ReconnectionStrategyTypeBuilder().buildReconnectionStrategyType();
          case POOLING_PROFILE_TYPE_KEY:
            return new PoolingProfileTypeBuilder().buildPoolingProfileType();
          case REDELIVERY_POLICY_TYPE_KEY:
            return new RedeliveryPolicyTypeBuilder().buildRedeliveryPolicyType();
          case DYNAMIC_EXPIRATION_TYPE_KEY:
            return new DynamicConfigExpirationTypeBuilder().buildExpirationPolicyType();
          case ERROR_MAPPINGS_TYPE_KEY:
            return new ErrorMappingsTypeBuilder().buildErrorMappingsType();

          case RECONNECT_SIMPLE_TYPE_KEY:
            return ((UnionType) new ReconnectionStrategyTypeBuilder().buildReconnectionStrategyType()).getTypes().get(0);
          case RECONNECT_FOREVER_TYPE_KEY:
            return ((UnionType) new ReconnectionStrategyTypeBuilder().buildReconnectionStrategyType()).getTypes().get(1);

          case STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_STREAM:
            return ((UnionType) new StreamingStrategyTypeBuilder().getByteStreamingStrategyType()).getTypes().get(0);
          case STREAMING_STRATEGY_REPEATABLE_FILE_STREAM:
            return ((UnionType) new StreamingStrategyTypeBuilder().getByteStreamingStrategyType()).getTypes().get(1);
          case STREAMING_STRATEGY_NON_REPEATABLE_STREAM:
            return ((UnionType) new StreamingStrategyTypeBuilder().getByteStreamingStrategyType()).getTypes().get(2);

          case STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_ITERABLE:
            return ((UnionType) new StreamingStrategyTypeBuilder().getObjectStreamingStrategyType()).getTypes().get(0);
          case STREAMING_STRATEGY_REPEATABLE_FILE_ITERABLE:
            return ((UnionType) new StreamingStrategyTypeBuilder().getObjectStreamingStrategyType()).getTypes().get(1);
          case STREAMING_STRATEGY_NON_REPEATABLE_ITERABLE:
            return ((UnionType) new StreamingStrategyTypeBuilder().getObjectStreamingStrategyType()).getTypes().get(2);

          default:
            throw new NoSuchElementException(key);
        }
      });

  public static void addReconnectionConfigParameter(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(RECONNECTION_CONFIG_PARAMETER_NAME);
    parameter.setDescription(RECONNECTION_CONFIG_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(METADATA_TYPES_CACHE.get(RECONNECTION_CONFIG_TYPE_KEY), false);
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

  public static void addPrimaryNodeParameter(SourceDeclaration declaration, boolean defaultValue) {
    ParameterDeclaration parameter = new ParameterDeclaration(PRIMARY_NODE_ONLY_PARAMETER_NAME);
    parameter.setDescription(PRIMARY_NODE_ONLY_PARAMETER_DESCRIPTION);
    parameter.setType(create(JAVA).booleanType().build(), false);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setDefaultValue(defaultValue);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());

    declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);
  }

  public static void addReconnectionStrategyParameter(ParameterizedDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(RECONNECTION_STRATEGY_PARAMETER_NAME);
    parameter.setDescription(RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(METADATA_TYPES_CACHE.get(RECONNECTION_STRATEGY_TYPE_KEY), false);
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
    parameter.setType(METADATA_TYPES_CACHE.get(POOLING_PROFILE_TYPE_KEY), false);
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
    parameter.setType(METADATA_TYPES_CACHE.get(REDELIVERY_POLICY_TYPE_KEY), false);
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
    parameter.setType(METADATA_TYPES_CACHE.get(DYNAMIC_EXPIRATION_TYPE_KEY), false);
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

  public static ParameterDeclaration addErrorMappings(OperationDeclaration operation) {
    ParameterDeclaration parameter = new ParameterDeclaration(ERROR_MAPPINGS_PARAMETER_NAME);
    parameter.setDescription(ERROR_MAPPINGS_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(METADATA_TYPES_CACHE.get(ERROR_MAPPINGS_TYPE_KEY), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ERROR_MAPPING_TAB).build());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_ERROR_MAPPING_QNAME));
    parameter.addModelProperty(new SinceMuleVersionModelProperty("4.4.0"));
    markAsInfrastructure(parameter, 12);

    final ParameterGroupDeclaration errorMappingsGroup = operation.getParameterGroup(ERROR_MAPPINGS);
    errorMappingsGroup.showInDsl(false);
    errorMappingsGroup.addParameter(parameter);

    return parameter;
  }

  private static void markAsInfrastructure(ParameterDeclaration parameter, int sequence) {
    parameter.addModelProperty(new InfrastructureParameterModelProperty(sequence));
  }

  /**
   * Creates a {@link MetadataType} for a known infrastructure type.
   *
   * @param infrastructureTypeName the name of the infrastructure type to create a {@link MetadataType} for.
   * @return the created {@link MetadataType}, or a cached one if it was already created.
   *
   * @since 1.4
   */
  public static MetadataType getInfrastructureParameterType(String infrastructureTypeName) {
    return METADATA_TYPES_CACHE.get(infrastructureTypeName);
  }
}

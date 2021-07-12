/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_DESCRIPTION;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;

/**
 * Creates instances of {@link MetadataType} which represent a reconnection strategy
 *
 * @since 1.0
 */
public final class ReconnectionStrategyTypeBuilder extends InfrastructureTypeBuilder {

  public static final String COUNT = "count";
  public static final String FREQUENCY = "frequency";
  public static final String RECONNECT_ALIAS = "reconnect";
  public static final String RECONNECT_FOREVER_ALIAS = "reconnect-forever";
  public static final String BLOCKING = "blocking";
  public static final String RECONNECTION_STRATEGY = "ReconnectionStrategy";
  public static final String RECONNECTION_CONFIG = "Reconnection";

  /**
   * @return a {@link MetadataType} representation of a reconnection configuration
   */
  public MetadataType buildReconnectionConfigType() {
    BaseTypeBuilder typeBuilder = create(JAVA);
    ObjectTypeBuilder type = create(JAVA).objectType()
        .id(RECONNECTION_CONFIG)
        .with(new InfrastructureTypeAnnotation());

    addBooleanField(type, typeBuilder, "failsDeployment", RECONNECTION_CONFIG_PARAMETER_DESCRIPTION, false);
    type.addField().key("reconnectionStrategy")
        .description("The reconnection strategy to use")
        .value(buildReconnectionStrategyType());

    return type.build();
  }

  /**
   * @return a {@link MetadataType} representation of a retry policy
   */
  public MetadataType buildReconnectionStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);
    return create(JAVA).unionType()
        .id(RECONNECTION_STRATEGY)
        .of(getSimpleRetryType(typeBuilder))
        .of(getForeverRetryType(typeBuilder))
        .with(new InfrastructureTypeAnnotation())
        .build();
  }

  private TypeBuilder getSimpleRetryType(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder retryType = typeBuilder.objectType()
        .id(RECONNECT_ALIAS)
        .with(new InfrastructureTypeAnnotation());

    addFrequencyField(retryType, typeBuilder);
    addBlockingField(retryType, typeBuilder);
    addIntField(retryType, typeBuilder, COUNT, "How many reconnection attempts to make", 2);

    return retryType;
  }

  private void addFrequencyField(ObjectTypeBuilder retryType, BaseTypeBuilder typeBuilder) {
    addLongField(retryType, typeBuilder, FREQUENCY, "How often (in ms) to reconnect", 2000L);
  }

  private TypeBuilder getForeverRetryType(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder retryType = typeBuilder.objectType()
        .id(RECONNECT_FOREVER_ALIAS)
        .with(new InfrastructureTypeAnnotation());
    addFrequencyField(retryType, typeBuilder);
    addBlockingField(retryType, typeBuilder);

    return retryType;
  }

  private void addBlockingField(ObjectTypeBuilder retryType, BaseTypeBuilder typeBuilder) {
    addBooleanField(retryType, typeBuilder, BLOCKING,
                    "If false, the reconnection strategy will run in a separate, non-blocking thread",
                    true);
  }
}

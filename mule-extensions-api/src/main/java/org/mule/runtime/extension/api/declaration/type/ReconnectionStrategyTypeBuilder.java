/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;

import java.util.Map;

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

  /**
   * @return a {@link MetadataType} representation of a retry policy
   */
  public MetadataType buildReconnectionStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);

    return create(JAVA).unionType()
        .of(getSimpleRetryType(typeBuilder))
        .of(getForeverRetryType(typeBuilder))
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation(RECONNECTION_STRATEGY))
        .build();
  }

  private TypeBuilder getSimpleRetryType(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder retryType = typeBuilder.objectType()
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation(RECONNECT_ALIAS));

    addFrequencyField(retryType, typeBuilder);
    addIntField(retryType, typeBuilder, COUNT, "How many reconnection attempts to make", 2);
    addBlockingField(retryType, typeBuilder);

    return retryType;
  }

  private void addFrequencyField(ObjectTypeBuilder retryType, BaseTypeBuilder typeBuilder) {
    addLongField(retryType, typeBuilder, FREQUENCY, "How often (in ms) to reconnect", 2000L);
  }

  private TypeBuilder getForeverRetryType(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder retryType = typeBuilder.objectType()
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation(RECONNECT_FOREVER_ALIAS));
    addFrequencyField(retryType, typeBuilder);

    return retryType;
  }

  private void addBlockingField(ObjectTypeBuilder retryType, BaseTypeBuilder typeBuilder) {
    addBooleanField(retryType, typeBuilder, BLOCKING,
                    "If false, the reconnection strategy will run in a separate, non-blocking thread",
                    true);
  }

}

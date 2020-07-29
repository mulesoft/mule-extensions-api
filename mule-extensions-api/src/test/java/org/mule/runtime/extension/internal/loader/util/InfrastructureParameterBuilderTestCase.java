/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder.RECONNECT_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder.RECONNECT_FOREVER_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.NON_REPEATABLE_BYTE_STREAM_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.NON_REPEATABLE_OBJECTS_STREAM_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.REPEATABLE_FILE_STORE_BYTES_STREAM_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.REPEATABLE_FILE_STORE_OBJECTS_STREAM_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.REPEATABLE_IN_MEMORY_BYTES_STREAM_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder.REPEATABLE_IN_MEMORY_OBJECTS_STREAM_ALIAS;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.RECONNECT_FOREVER_TYPE_KEY;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.RECONNECT_SIMPLE_TYPE_KEY;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_STREAM;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.getInfrastructureParameterType;

import org.mule.metadata.api.annotation.TypeIdAnnotation;

import org.junit.Test;

public class InfrastructureParameterBuilderTestCase {

  @Test
  public void simpleReconnect() {
    assertThat(getInfrastructureParameterType(RECONNECT_SIMPLE_TYPE_KEY)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null), is(RECONNECT_ALIAS));
  }

  @Test
  public void reconnectForever() {
    assertThat(getInfrastructureParameterType(RECONNECT_FOREVER_TYPE_KEY)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null), is(RECONNECT_FOREVER_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableInMemoryStream() {
    assertThat(getInfrastructureParameterType(STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_STREAM)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null),
               is(REPEATABLE_IN_MEMORY_BYTES_STREAM_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableFileStream() {
    assertThat(getInfrastructureParameterType(InfrastructureParameterBuilder.STREAMING_STRATEGY_REPEATABLE_FILE_STREAM)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null),
               is(REPEATABLE_FILE_STORE_BYTES_STREAM_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableNonRepeatableStream() {
    assertThat(getInfrastructureParameterType(InfrastructureParameterBuilder.STREAMING_STRATEGY_NON_REPEATABLE_STREAM)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null), is(NON_REPEATABLE_BYTE_STREAM_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableInMemoryIterable() {
    assertThat(getInfrastructureParameterType(InfrastructureParameterBuilder.STREAMING_STRATEGY_REPEATABLE_IN_MEMORY_ITERABLE)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null),
               is(REPEATABLE_IN_MEMORY_OBJECTS_STREAM_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableFileIteralbe() {
    assertThat(getInfrastructureParameterType(InfrastructureParameterBuilder.STREAMING_STRATEGY_REPEATABLE_FILE_ITERABLE)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null),
               is(REPEATABLE_FILE_STORE_OBJECTS_STREAM_ALIAS));
  }

  @Test
  public void streamingStrategyRepeatableNonRepeatableIterable() {
    assertThat(getInfrastructureParameterType(InfrastructureParameterBuilder.STREAMING_STRATEGY_NON_REPEATABLE_ITERABLE)
        .getAnnotation(TypeIdAnnotation.class).map(tid -> tid.getValue()).orElse(null), is(NON_REPEATABLE_OBJECTS_STREAM_ALIAS));
  }
}

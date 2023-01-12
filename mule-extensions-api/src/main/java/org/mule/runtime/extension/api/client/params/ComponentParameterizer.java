/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.params;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.util.DataSize;

public interface ComponentParameterizer<T extends ComponentParameterizer> {

  /**
   * Specifies the name of a config element this component refers to
   *
   * @param configurationName a configuration name
   * @return {@code this} instance
   */
  T withConfigRef(String configurationName);

  /**
   * Sets a parameter with a given value, automatically determining the group the parameter belongs to.
   *
   * @param parameterName the name of the parameter within the {@code paramGroupName} group to set.
   * @param value         the value of the parameter to set
   * @return {@code this} instance
   */
  T withParameter(String parameterName, Object value);

  /**
   * Sets a parameter with a given value.
   *
   * @param parameterGroup the name of the group containing the parameter to set.
   * @param parameterName  the name of the parameter within the {@code paramGroupName} group to set.
   * @param value          the value of the parameter to set
   * @return {@code this} instance
   */
  T withParameter(String parameterGroup, String parameterName, Object value);

  /**
   * Specifies that in the case of a {@link ConnectionException}, the connection is to be re-established and the operation retried
   * up to {@code maxAttempts} times, with an interval of {@code frequency} milliseconds.
   * <p>
   * If no reconnection strategy is specified (either through this or similar methods), then no reconnection is attempted at all.
   *
   * @param frequency   the attempts interval in milliseconds
   * @param maxAttempts the max number of attempts to be performed.
   * @return {@code this} instance
   */
  T withSimpleReconnection(int frequency, int maxAttempts);

  /**
   * Specifies that in the case of a {@link ConnectionException}, the connection is to be re-established and the operation
   * indefinitely until successful
   * <p>
   * If no reconnection strategy is specified (either through this or similar methods), then no reconnection is attempted at all.
   *
   * @param frequency the attempts interval in milliseconds
   * @return {@code this} instance
   */
  T reconnectingForever(int frequency);

  /**
   * Specifies that the operation is to used whatever repeatable streaming strategy is default for the current execution context.
   * <p>
   * If this concept does not apply to the executed operation (either because it uses iterables instead of streams or because it
   * doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @return {@code this} instance
   */
  T withDefaultRepeatableStreaming();

  /**
   * Specifies that the operation is to use in-memory repeatable streaming
   * <p>
   * If this concept does not apply to the executed operation (either because it uses iterables instead of streams or because it
   * doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @param initialBufferSize   the buffer's initial size. Must be greater than zero bytes.
   * @param bufferSizeIncrement the size that the buffer should gain each time it is expanded. A value of zero bytes means no
   *                            expansion. Cannot be negative byte size.
   * @param maxBufferSize       the maximum amount of space that the buffer can grow to. Use {@code null} for unbounded buffers
   * @return {@code this} instance
   */
  T withInMemoryRepeatableStreaming(DataSize initialBufferSize,
                                    DataSize bufferSizeIncrement,
                                    DataSize maxBufferSize);

  /**
   * Specifies that the operation is to use file based repeatable streaming
   * <p>
   * If this concept does not apply to the executed operation (either because it uses iterables instead of streams or because it
   * doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @param maxInMemorySize the maximum amount of data to be held in memory before information is rolled over to disk
   * @return {@code this} instance
   */
  T withFileStoreRepeatableStreaming(DataSize maxInMemorySize);

  /**
   * Specifies that the operation is to used whatever repeatable iterables strategy is default for the current execution context.
   * <p>
   * If this concept does not apply to the executed operation (either because it uses bytes streaming instead of iterables or
   * because it doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @return {@code this} instance
   */
  T withDefaultRepeatableIterables();

  /**
   * Specifies that the operation is to use in-memory repeatable iterables
   * <p>
   * If this concept does not apply to the executed operation (either because it uses bytes streams instead of iterables or
   * because it doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @param initialBufferSize   the buffer's initial size. Must be greater than zero
   * @param bufferSizeIncrement the size that the buffer should gain each time it is expanded. A value of zero means no expansion.
   *                            Cannot be lower than zero.
   * @param maxBufferSize       the maximum amount of space that the buffer can grow to. Use {@code null} for unbounded buffers
   * @return {@code this} instance
   */
  T withInMemoryRepeatableIterables(int initialBufferSize, int bufferSizeIncrement, int maxBufferSize);

  /**
   * Specifies that the operation is to use file based repeatable streaming
   * <p>
   * If this concept does not apply to the executed operation (either because it uses bytes streams instead of iterables or
   * because it doesn't stream at all), then calling this method has no effect.
   * <p>
   * If no repeatable streaming strategy is specified (either through this or similar methods), then streaming results will not be
   * made repeatable
   *
   * @param maxInMemoryInstances maxInMemoryInstances the maximum amount of space that the buffer can grow to. Use {@code null}
   *                             for unbounded buffers
   * @return {@code this} instance
   */
  T withFileStoreRepeatableIterables(int maxInMemoryInstances);
}

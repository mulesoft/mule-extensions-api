/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Contains context associated to each polling execution, and allows to dispatch items for execution through the
 * {@link #accept(Consumer)} method. Depending on how the supplied {@link PollItem} is configured, the runtime will automatically
 * apply watermarking, idempotency, etc.
 *
 * @param <T> the generic type for the generated message's payload
 * @param <A> the generic type for the generated message's attributes
 * @see PollItem
 * @since 1.1
 */
@NoImplement
public interface PollContext<T, A> {

  /**
   * The possible outcomes of submiting an item for processing through the {@link #accept(Consumer)} method
   */
  enum PollItemStatus {

    /**
     * The item was accepted and has been scheduled for execution
     */
    ACCEPTED,

    /**
     * The item was rejected because watermarking was enabled and it was filtered on that ground
     */
    FILTERED_BY_WATERMARK,

    /**
     * The item was rejected because idempotency was enabled and another thread or node is already processing this item
     */
    ALREADY_IN_PROCESS,

    /**
     * The item was rejected because the source has received the stop signal
     */
    SOURCE_STOPPING
  }

  /**
   * Submits an item to be processed. The runtime will supply a {@link PollItem} in which the {@link Result} and other options
   * will be set
   *
   * @param consumer a {@link Consumer} of a {@link PollItem}
   * @return a {@link PollItemStatus}
   */
  PollItemStatus accept(Consumer<PollItem<T, A>> consumer);

  /**
   * The current watermark value at the moment of starting the poll. If watermarking is disabled or this is the first execution,
   * the value will be {@link Optional#empty()}
   *
   * @return an optional watermark value
   */
  Optional<Serializable> getWatermark();

  /**
   * Whether the source has received the stopping signal. It is a good practice to check for this condition before engaging on a
   * time consuming task.
   *
   * @return Whether the source has received the stopping signal or not.
   */
  boolean isSourceStopping();

  /**
   * Allows to set a custom {@link Comparator} to evaluate watermark values. If not specified and {@link Comparable} values are
   * used as watermarks, then this method is only optional. Otherwise, exceptions will be thrown if a proper comparator was not
   * set.
   *
   * @param comparator a custom {@link Comparator}
   * @throws IllegalArgumentException if the {@code comparator} is {@code null}
   */
  void setWatermarkComparator(Comparator<? extends Serializable> comparator);

  /**
   * Equivalent to {@link SourceCallback#onConnectionException(ConnectionException)}. Should be used in the same circumstances
   *
   * @param e a {@link ConnectionException}
   */
  void onConnectionException(ConnectionException e);

  /**
   * An item obtained through a poll
   *
   * @param <T> the generic type for the generated message's payload
   * @param <A> the generic type for the generated message's attributes
   * @since 1.1
   */
  interface PollItem<T, A> {

    /**
     * @return The {@link SourceCallbackContext}
     */
    SourceCallbackContext getSourceCallbackContext();

    /**
     * Sets the item content in the form of a {@link Result}
     *
     * @param result a {@link Result}
     * @return {@code this} instance
     * @throws IllegalArgumentException if the {@code result} is {@code null}
     */
    PollItem<T, A> setResult(Result<T, A> result);

    /**
     * Sets the watermark value associated to this item. This is the value that will be used to evaluate this item against the
     * current watermark. If not provided, then no watermark filter will be applied.
     * <p>
     * Remember that if the {@code watermark} value is not {@link Comparable}, then a custom {@link Comparator} should be provided
     * through {@link PollContext#setWatermarkComparator(Comparator)}
     *
     * @param watermark a watermark value
     * @return {@code this} instance
     */
    PollItem<T, A> setWatermark(Serializable watermark);

    /**
     * Associates this item to a unique identifier. If provided, the runtime will make sure that no other thread or cluster node
     * is processing an item of the same id, providing processing idempotency. If not set, no such guarantees are offered.
     *
     * @param id a unique identifier
     * @return {@code this} instance
     */
    PollItem<T, A> setId(String id);
  }
}

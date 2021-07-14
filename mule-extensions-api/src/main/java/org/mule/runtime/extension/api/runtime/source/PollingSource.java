/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.function.Consumer;

/**
 * A Special kind of {@link Source} which automatically handles polling, idempotency and watermarking. As a result of the poll,
 * many items can be obtained and dispatched for execution, each one as a standalone {@link Message}.
 * <p>
 * The SDK will automatically add a {@code &lt;scheduling-strategy&gt;} parameter and the runtime will use that strategy to
 * automatically schedule executions of the {@link #poll(PollContext)} method. The {@link PollContext} will be used to dispatch
 * the obtained items, optionally enabling features like watermarking and idempotency.
 *
 * @param <T> the generic type for the generated message's payload
 * @param <A> the generic type for the generated message's attributes
 * @since 1.1
 */
@MinMuleVersion("4.1")
public abstract class PollingSource<T, A> extends Source<T, A> {

  public static final String OS_NAME_PREFIX = "_pollingSource_";
  public static final String OS_NAME_MASK = OS_NAME_PREFIX + "%s/%s";

  public static final String WATERMARK_OS_NAME_SUFFIX = "watermark";
  public static final String RECENTLY_PROCESSED_IDS_OS_NAME_SUFFIX = "recently-processed-ids";
  public static final String IDS_ON_UPDATED_WATERMARK_OS_NAME_SUFFIX = "ids-on-updated-watermark";

  public static final String WATERMARK_ITEM_OS_KEY = "watermark";
  public static final String UPDATED_WATERMARK_ITEM_OS_KEY = "updatedWatermark";

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onStart(SourceCallback<T, A> sourceCallback) throws MuleException {
    doStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void onStop() {
    doStop();
  }

  /**
   * Implement this method to perform custom starting logic. Remember that the runtime is polling automatically
   *
   * @throws MuleException
   */
  protected abstract void doStart() throws MuleException;

  /**
   * Implement this method to perform custom stopping logic.
   */
  protected abstract void doStop();

  /**
   * This method is responsible for obtaining the items to be dispatched and communicate them through the {@code pollContext}
   * using the {@link PollContext#accept(Consumer)} method.
   * <p>
   * See the {@link PollContext} documentation for more information on how to use it and the options available
   *
   * @param pollContext the polling context
   */
  public abstract void poll(PollContext<T, A> pollContext);

  /**
   * If one of the items dispatched through {@link PollContext#accept(Consumer)} were rejected (either by watermarking,
   * idempotency, server overload, etc.), this method is to be used to released any resources associated to the given
   * {@code result}.
   *
   * @param result          a rejected item
   * @param callbackContext the associated {@link SourceCallbackContext}
   */
  public abstract void onRejectedItem(Result<T, A> result, SourceCallbackContext callbackContext);

}

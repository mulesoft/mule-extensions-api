/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import static java.util.Optional.empty;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class PollingSource<T, A> extends Source<T, A> {

  @Override
  public final void onStart(SourceCallback<T, A> sourceCallback) throws MuleException {
    doStart();
  }

  @Override
  public final void onStop() {
    doStop();
  }

  protected abstract void doStart() throws MuleException;

  protected abstract void doStop();

  public abstract List<Result<T, A>> poll();

  public abstract void releaseRejectedResource(Result<T, A> result);

  public void onResult(Result<T, A> result, SourceCallbackContext sourceCallbackContext) {}

  public Optional<Function<Result<T, A>, String>> getIdentityResolver() {
    return empty();
  }

  public <W extends Serializable> Optional<WatermarkHandler<T, A, W>> getWatermarkHandler() {
    return empty();
  }
}

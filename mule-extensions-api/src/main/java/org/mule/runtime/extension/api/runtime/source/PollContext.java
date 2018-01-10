/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

public interface PollContext<T, A> {

  void accept(Consumer<PollItem<T, A>> consumer);

  Optional<Serializable> getWatermark();

  void setWatermarkComparator(Comparator<? extends Serializable> comparator);

  interface PollItem<T, A> {

    SourceCallbackContext getSourceCallbackContext();

    PollItem<T, A> setResult(Result<T, A> result);

    PollItem<T, A> setWatermark(Serializable watermark);

    PollItem<T, A> setId(String id);
  }
}

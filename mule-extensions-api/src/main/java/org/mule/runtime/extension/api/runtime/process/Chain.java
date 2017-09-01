/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ComposableModel;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.extension.api.runtime.operation.Result;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The executable component associated to a {@link NestedChainModel}.
 * For a given {@link ComposableModel} to be able to receive a chain of nested processor and execute it,
 * it must receive a {@link Chain}.
 * This processor chain will be represented in the owner's {@link ComponentModel} by a {@link NestedChainModel}.
 *
 * @since 1.0
 */
public interface Chain {

  /**
   *   
   * @param onSuccess
   * @param onError
   */
  void process(Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

  void process(Object payload, Object attributes, Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

  void process(Result input, Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

}

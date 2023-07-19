/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.route;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A {@link Chain} allows a given {@link OperationModel Operation} to receive a chain of message processors to be executed as part
 * of the operation's execution.
 * <p>
 * When executing the {@link Chain}, all its inner components will be executed in the order declared by the user, using the output
 * of one processor as the input of the next one in the {@link Chain}. The execution of the {@link Chain} is completed either when
 * all the components were invoked successfully or when an error occurs in one processor, and it is propagated to the
 * {@link Chain}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface Chain {

  /**
   * Executes the chain of components starting with the same input message that it's container scope received.
   * <p>
   * {@code onSuccess} callback will be invoked with the output {@link Result} of the last component in the {@link Chain} if no
   * error occurred after all the components were executed exactly once.
   * <p>
   * {@code onError} callback will be invoked with the exception propagated by the first failing component, along with the last
   * output {@link Result} available. The given {@link Result} will be the same that was used as input of the failing component.
   *
   * @param onSuccess the callback to be executed when a successful execution is completed by the {@link Chain}
   * @param onError   the callback to be executed when an error occurs during the execution of the {@link Chain} components
   */
  void process(Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

  /**
   * Executes the chain of components starting with the given {@code payload} and {@code attributes} as {@link Message}.
   * <p>
   * {@code onSuccess} callback will be invoked with the output {@link Result} of the last component in the {@link Chain} if no
   * error occurred after all the components were executed exactly once.
   * <p>
   * {@code onError} callback will be invoked with the exception propagated by the first failing component, along with the last
   * output {@link Result} available. The given {@link Result} will be the same that was used as input of the failing component.
   *
   * @param onSuccess the callback to be executed when a successful execution is completed by the {@link Chain}
   * @param onError   the callback to be executed when an error occurs during the execution of the {@link Chain} components
   */
  void process(Object payload, Object attributes, Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

  /**
   * Executes the chain of components starting with the given {@link Result} data as input {@link Message}. This method should be
   * used whenever the input of this {@link Chain} is the output of another component execution.
   * <p>
   * {@code onSuccess} callback will be invoked with the output {@link Result} of the last component in the {@link Chain} if no
   * error occurred after all the components were executed exactly once.
   * <p>
   * {@code onError} callback will be invoked with the exception propagated by the first failing component, along with the last
   * output {@link Result} available. The given {@link Result} will be the same that was used as input of the failing component.
   *
   * @param onSuccess the callback to be executed when a successful execution is completed by the {@link Chain}
   * @param onError   the callback to be executed when an error occurs during the execution of the {@link Chain} components
   */
  void process(Result input, Consumer<Result> onSuccess, BiConsumer<Throwable, Result> onError);

}

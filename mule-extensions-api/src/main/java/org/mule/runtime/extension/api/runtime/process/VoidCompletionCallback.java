/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

/**
 * This callback is how non blocking routers notify the end of its execution when no result is produced.
 * <p>
 * In order to implement a void non blocking router, the method needs to:
 * <p>
 * <ul>
 * <li>Have a void return type</li>
 * <li>Have an argument of this type</li>
 * </ul>
 * <p>
 * When the non blocking operation has finished, it has to notify its completion either by
 * invoking the {@link #success()} or {@link #error(Throwable)} methods.
 * Only then will the operation be considered as completed and the next processor in the
 * pipeline will be executed.
 * <p>
 * For example, a Void router can be declared as:
 * <p>
 * <pre>
 *
 *  public void enricher(WhenRoute when, @Optional DefaultRoute defaultRoute, VoidCompletionCallback callback) {
 *     if (when.shouldExecute()) {
 *        when.getChain().process(r -> callback.success(),
 *                                (e, r) -> callback.error(e));
 *     } else if (other != null && other.shouldExecute()) {
 *        other.getChain().process(r -> callback.success(),
 *                                 (e, r) -> callback.error(e));
 *     } else {
 *        callback.error(new IllegalArgumentException("No route executed"));
 *     }
 *  }
 * </pre>
 * <p>
 * As you can see, the result of the Route being executed is ignored, and the
 *
 * @since 1.1
 */
public interface VoidCompletionCallback {

  /**
   * This method is to be invoked when the Operation execution is completed
   */
  void success();

  /**
   * This method is not be invoked when the Operation failed to execute.
   *
   * @param e the exception found
   */
  void error(Throwable e);


}

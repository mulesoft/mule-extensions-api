/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.meta.model.ComponentModel;

/**
 * This callback is how a {@link ComponentModel components} notifies that its execution is completed,
 * without producing any output.
 * <p>
 * When the execution of the component has finished, it has to notify the completion either by
 * invoking {@link #success()} or {@link #error(Throwable)} methods.
 * Only then will the component's execution be considered as completed and the next processor in the
 * pipeline will be executed.
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

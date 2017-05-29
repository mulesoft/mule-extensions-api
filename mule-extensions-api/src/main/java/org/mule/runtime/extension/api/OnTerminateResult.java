/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import org.mule.runtime.api.message.Error;
import org.mule.runtime.extension.api.annotation.execution.OnTerminate;

import java.util.function.Consumer;

/**
 * Result for the {@link OnTerminate} callbacks.
 * This result provides an easy way and common way to discern the different kinds of states that a
 *
 * @since 1.0
 */
@FunctionalInterface
public interface OnTerminateResult {

  void execute(Runnable onSuccess, Consumer<Error> onParameterResolutionError, Consumer<Error> onResponseError);

}

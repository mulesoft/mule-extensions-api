/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import org.mule.runtime.api.message.Error;

import java.util.function.Consumer;

/**
 * mule-alltogether
 *
 * @author Esteban Wasinger (http://github.com/estebanwasinger)
 */
public interface OnTerminateCallback {

  void execute(Consumer<Void> onSuccess, Consumer<Error> onParameterResolutionError, Consumer<Error> onResponseError);

  void execute(Consumer<Error> onParameterResolutionError, Consumer<Error> onResponseError);
}

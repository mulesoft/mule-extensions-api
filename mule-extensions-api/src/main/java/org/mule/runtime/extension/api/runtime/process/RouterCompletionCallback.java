/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.route.Route;

/**
 * This callback is how {@link ComponentModel components} receiving {@link Route}s notify their outcome.
 * <p>
 * In order to implement a Router (that is, an operation that receives one or more {@link Route}s), the method needs to:
 * <p>
 * <ul>
 * <li>Have a void return type</li>
 * <li>Have an argument of {@link RouterCompletionCallback} type</li>
 * </ul>
 * <p>
 * When the execution of the router (and therefore one or more of its routes) has finished,
 * it has to notify the result either by invoking the {@link #success(Result)} or {@link #error(Throwable)} methods.
 * Only then will the router's execution be considered as completed and the next processor in the
 * pipeline will be executed.
 * <b>If the {@link #success(Result)} or {@link #error(Throwable)} methods are invoked before any of the nested {@link Route}s
 * is completed, the {@link Result} of the nested execution will be lost and never propagated.</b>
 *
 * @since 1.0
 */
public interface RouterCompletionCallback extends CompletionCallback<Object, Object> {


}

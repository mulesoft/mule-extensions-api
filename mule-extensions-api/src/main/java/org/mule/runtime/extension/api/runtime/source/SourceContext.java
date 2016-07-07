/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.api.execution.CompletionHandler;
import org.mule.runtime.api.execution.ExceptionCallback;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.extension.api.runtime.ConfigurationInstance;
import org.mule.runtime.extension.api.runtime.MessageHandler;

/**
 * Provides configuration and collaboratos for a {@link Source}
 *
 * @param <Payload>    the generic type for the generated message's payload
 * @param <A> the generic type for the generated message's attributes
 * @since 1.0
 */
public interface SourceContext<Payload, A extends Attributes>
{

    /**
     * @return the {@link MessageHandler} to be used for processing the generated messages
     */
    MessageHandler<Payload, A> getMessageHandler();

    /**
     * Provides the {@link ExceptionCallback} on which exceptions are to be notified.
     * <p>
     * Notice that this callback is for notifying the runtime about problems which actually
     * belong to the source, such as loosing connectivity. This is not to be confused
     * with the {@link CompletionHandler#onFailure(Throwable)} method which can be invoked
     * through the {@link #getMessageHandler()} method, which is used to handle message
     * errors which are actually related to the processing of it rather than the source
     *
     * @return a {@link ExceptionCallback}
     */
    ExceptionCallback<Void, Throwable> getExceptionCallback();

    /**
     * @return the {@link ConfigurationInstance} to which the {@link Source} is associated
     */
    ConfigurationInstance<Object> getConfigurationInstance();
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.api.execution.CompletionHandler;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.api.message.MuleEvent;
import org.mule.runtime.api.message.MuleMessage;

/**
 * Handles the processing of a {@link MuleMessage} and notifies the result
 * of such process using a {@link CompletionHandler}.
 * <p>
 * Although this contract does not guarantee the processing being synchronous
 * or asynchronous, it is meant to be used on an asynchronous way.
 * <p>
 * Implementations are to be reusable and thread-safe
 *
 * @param <Payload>    the generic type for the handled message's payload
 * @param <A> the generic type for the handled message's attributes
 * @since 1.0
 */
public interface MessageHandler<Payload, A extends Attributes>
{

    /**
     * Handles the {@code message} and notifies the result using the given {@code completionHandler}
     *
     * @param message           the {@link MuleMessage} to be handled
     * @param completionHandler the {@link CompletionHandler} on which the result is to be notified
     */
    //TODO: MULE-8946: this should actually receive a messaging exception
    void handle(MuleMessage message, CompletionHandler<MuleEvent, Exception, MuleEvent> completionHandler);

    /**
     * Handles the {@code message} without notifying the result. Useful to implement fire and forget use cases
     *
     * @param message the {@link MuleMessage} to be handled
     */
    void handle(MuleMessage message);

}

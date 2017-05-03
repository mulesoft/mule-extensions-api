/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;


import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Initialisable;

import java.io.InputStream;


/**
 * This interface allows that different transports (such as JMS or HTTP) behave the same way when dispatching a
 * Web Service operation message.
 *
 * @since 1.0
 */
public interface MessageDispatcher extends Initialisable, Disposable {

  /**
   * Sends off a Soap Request to a destination and returns it's response.
   *
   * @param message the soap envelope to be sent.
   * @param context a {@link DispatchingContext} containing all the required information to deliver the message.
   * @return a {@link DispatcherResponse} with the content returned by the transport and it's corresponding Content-Type.
   */
  DispatcherResponse dispatch(InputStream message, DispatchingContext context);
}

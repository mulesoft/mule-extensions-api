/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.message;


/**
 * This interface allows that different transports (such as JMS or HTTP) behave the same way when dispatching a Web Service
 * operation message.
 *
 * @since 1.0
 */
public interface MessageDispatcher {

  /**
   * Sends off a Soap Request to a destination and returns it's response.
   *
   * @param request a {@link DispatchingRequest} containing all the required information to deliver the message.
   * @return a {@link DispatchingResponse} with the content returned by the transport and it's corresponding Content-Type.
   */
  DispatchingResponse dispatch(DispatchingRequest request);

}

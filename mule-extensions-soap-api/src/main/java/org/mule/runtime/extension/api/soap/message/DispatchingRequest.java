/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;


import java.io.InputStream;
import java.util.Map;

/**
 * A context with all the required information for a {@link MessageDispatcher} to properly dispatch the Soap message.
 *
 * @since 1.0
 */
public class DispatchingRequest extends BaseDispatchingContext {

  private final String address;

  public DispatchingRequest(InputStream message, String address, Map<String, String> headers) {
    super(message, headers);
    this.address = address;
  }

  /**
   * @return the address where the service lives.
   */
  public String getAddress() {
    return address;
  }
}

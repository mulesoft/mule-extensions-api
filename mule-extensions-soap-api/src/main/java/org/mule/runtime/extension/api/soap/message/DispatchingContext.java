/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;


import java.util.Map;

/**
 * A context with all the required information for a {@link MessageDispatcher} to properly dispatch the Soap message.
 *
 * @since 1.0
 */
public class DispatchingContext {

  private final String address;
  private final String encoding;
  private final Map<String, String> headers;

  public DispatchingContext(String address, String encoding, Map<String, String> headers) {
    this.address = address;
    this.encoding = encoding;
    this.headers = headers;
  }

  /**
   * @return the address where the service lives.
   */
  public String getAddress() {
    return address;
  }

  /**
   * @return the outgoing message encoding.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @return a set of additional headers to be bundled with the request.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }
}

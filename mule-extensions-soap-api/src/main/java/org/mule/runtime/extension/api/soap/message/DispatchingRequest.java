/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

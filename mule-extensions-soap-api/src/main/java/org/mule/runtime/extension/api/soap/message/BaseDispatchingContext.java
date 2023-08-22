/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;


import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * Base class for messages that are sent or retrieved via {@link MessageDispatcher}s.
 *
 * @since 1.0
 */
abstract class BaseDispatchingContext {

  private static final String CONTENT_TYPE = "Content-Type";

  private final InputStream content;
  private final Map<String, String> headers;

  BaseDispatchingContext(InputStream content, Map<String, String> headers) {
    this.content = content;
    this.headers = headers;
  }

  /**
   * @return the raw message content.
   */
  public InputStream getContent() {
    return content;
  }

  /**
   * @return the content-type of the raw WS response content.
   */
  public String getContentType() {
    return getHeader(CONTENT_TYPE)
        .orElseGet(() -> getHeader(CONTENT_TYPE.toLowerCase())
            .orElseThrow(() -> new IllegalStateException("No " + CONTENT_TYPE + "boundled in the message")));
  }

  /**
   * @return the output headers returned after dispatching the soap message.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * @param headerName the name of the header which values are requested
   * @return an {@link Optional} carrying the value of the header, {@link Optional#empty()} if the header does not exist.
   */
  public Optional<String> getHeader(String headerName) {
    return Optional.ofNullable(headers.get(headerName));
  }
}

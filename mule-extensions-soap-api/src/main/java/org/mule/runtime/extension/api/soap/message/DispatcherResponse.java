/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * A simple object that carries the information retrieved after the message was dispatched with a {@link MessageDispatcher}.
 *
 * @since 1.0
 */
public class DispatcherResponse {

  private final String contentType;
  private final InputStream content;
  private final Map<String, ? extends List<String>> headers;

  public DispatcherResponse(String contentType, InputStream content, Map<String, ? extends List<String>> headers) {
    this.contentType = contentType;
    this.content = content;
    this.headers = headers;
  }

  /**
   * @return the raw Web Service response content.
   */
  public InputStream getContent() {
    return content;
  }

  /**
   * @return the content-type of the raw WS response content.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return the output headers returned after dispatching the soap message.
   */
  public Map<String, ? extends List<String>> getHeaders() {
    return headers;
  }
}

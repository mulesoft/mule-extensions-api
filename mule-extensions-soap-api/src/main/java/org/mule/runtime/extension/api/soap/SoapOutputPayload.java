/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.api.metadata.TypedValue;
import java.io.InputStream;
import java.util.Map;

/**
 * A simple container object that carries the SOAP envelope information and the attachments bounded to the response.
 *
 * @since 1.0
 */
public class SoapOutputPayload {

  private final TypedValue<InputStream> body;
  private final Map<String, TypedValue<InputStream>> attachments;
  private final Map<String, String> headers;

  public SoapOutputPayload(TypedValue<InputStream> body,
                           Map<String, TypedValue<InputStream>> attachments,
                           Map<String, String> headers) {
    this.body = body;
    this.attachments = attachments;
    this.headers = headers;
  }

  /**
   * @return The xml response body. Represents the <SOAP:BODY> element
   */
  public TypedValue<InputStream> getBody() {
    return body;
  }

  /**
   * @return A set of attachments bounded to the response, an empty map if there is no attachments.
   */
  public Map<String, TypedValue<InputStream>> getAttachments() {
    return attachments;
  }

  /**
   * @return A set of XML SOAP headers. Represents the content inside the <SOAP:HEADERS> element.
   */
  public Map<String, String> getHeaders() {
    return headers;
  }
}

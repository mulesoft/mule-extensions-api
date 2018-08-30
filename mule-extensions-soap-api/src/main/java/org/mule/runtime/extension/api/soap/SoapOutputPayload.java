/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.apache.commons.io.IOUtils;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.streaming.CursorProvider;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static java.nio.charset.Charset.forName;
import static java.util.stream.Collectors.joining;

/**
 * A simple container object that carries the SOAP envelope information and the attachments bounded to the response.
 *
 * @since 1.0
 */
public class SoapOutputPayload {

  private static final Logger LOGGER = LoggerFactory.getLogger(SoapOutputPayload.class.getName());

  private final TypedValue<InputStream> body;
  private final Map<String, TypedValue<InputStream>> attachments;
  private final Map<String, TypedValue<String>> headers;

  public SoapOutputPayload(TypedValue<InputStream> body,
                           Map<String, TypedValue<InputStream>> attachments,
                           Map<String, TypedValue<String>> headers) {
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
  public Map<String, TypedValue<String>> getHeaders() {
    return headers;
  }

  @Override
  public String toString() {
    try {
      String hs = headers.values().stream().map(v -> "\"" + v.getValue() + "\"").collect(joining(",\n  "));
      String as = String.join(", ", attachments.keySet());
      return "{\n" +
          "body:" + getBodyString() + ",\n" +
          "headers: [" + hs + "]" + ",\n" +
          "attachments: [" + as + "]" + "\n" +
          "}";
    } catch (Exception e) {
      LOGGER.error("Error building SoapResponse string: " + e.getMessage(), e);
      return "Error building SoapResponse string";
    }
  }

  private String getBodyString() throws IOException {
    InputStream bodyVal = body.getValue();
    InputStream bodyStream = bodyVal instanceof CursorStreamProvider ? ((CursorStreamProvider) bodyVal).openCursor() : bodyVal;
    return IOUtils.toString(bodyStream, getBodyCharset());
  }

  private Charset getBodyCharset() {
    DataType dataType = body.getDataType();
    Charset defaultCharset = forName("UTF-8");
    return dataType != null ? dataType.getMediaType().getCharset().orElse(defaultCharset) : defaultCharset;
  }
}

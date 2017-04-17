/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import static org.mule.runtime.api.util.Preconditions.checkNotNull;
import org.mule.runtime.api.metadata.MediaType;

import java.io.InputStream;

/**
 * Represents and enables the construction of an attachment to be sent over SOAP.
 *
 * @since 1.0
 */
public final class SoapAttachment {

  /**
   * the content of the attachment.
   */
  private InputStream content;

  /**
   * the content type of the attachment content.
   */
  private MediaType contentType;

  public SoapAttachment(InputStream content, MediaType contentType) {
    checkNotNull(content, "Content cannot be null");
    checkNotNull(contentType, "Content Type cannot be null");
    this.content = content;
    this.contentType = contentType;
  }

  public SoapAttachment() {}

  /**
   * @return the content of the attachment.
   */
  public InputStream getContent() {
    return content;
  }

  /**
   * @return the content type of the attachment content.
   */
  public MediaType getContentType() {
    return contentType;
  }
}

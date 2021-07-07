/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.message;

import java.io.InputStream;
import java.util.Map;

/**
 * A simple object that carries the information retrieved after the message was dispatched with a {@link MessageDispatcher}.
 *
 * @since 1.0
 */
public class DispatchingResponse extends BaseDispatchingContext {

  public DispatchingResponse(InputStream content, Map<String, String> headers) {
    super(content, headers);
  }
}

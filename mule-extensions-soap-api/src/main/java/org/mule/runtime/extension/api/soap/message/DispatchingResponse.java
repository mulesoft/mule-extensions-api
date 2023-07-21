/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.collect.ImmutableMap.of;

import java.io.Serializable;
import java.util.Map;

/**
 * Contains the headers retrieved by the protocol after the request.
 *
 * @since 1.0
 */
public final class SoapAttributes implements Serializable {

  private static final long serialVersionUID = 4591210489306615571L;

  private final Map<String, String> protocolHeaders;

  public SoapAttributes(Map<String, String> protocolHeaders) {
    this.protocolHeaders = protocolHeaders != null ? copyOf(protocolHeaders) : of();
  }

  /**
   * @return a set of protocol headers bounded to the service response. i.e. HTTP Headers.
   */
  public Map<String, String> getProtocolHeaders() {
    return protocolHeaders;
  }
}

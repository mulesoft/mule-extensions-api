/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import static java.util.Collections.emptyList;
import static org.mule.runtime.api.util.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Builder pattern implementation to create {@link WebServiceDefinition} instances.
 *
 * @since 1.0
 */
public class WebServiceDefinitionBuilder {

  private String serviceId;
  private String friendlyName;
  private URL wsdlUrl;
  private URL address;
  private String service;
  private String port;
  private List<String> excludedOperations = emptyList();

  WebServiceDefinitionBuilder() {}

  public WebServiceDefinitionBuilder withId(String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  public WebServiceDefinitionBuilder withFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
    return this;
  }

  public WebServiceDefinitionBuilder withWsdlUrl(URL wsdlUrl) {
    this.wsdlUrl = wsdlUrl;
    return this;
  }

  public WebServiceDefinitionBuilder withAddress(URL address) {
    this.address = address;
    return this;
  }

  public WebServiceDefinitionBuilder withWsdlUrl(String wsdlUrl) {
    try {
      this.wsdlUrl = new URL(wsdlUrl);
      return this;
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("the provided WSDL URL is not a well formed URL: [" + wsdlUrl + "]", e);
    }
  }

  public WebServiceDefinitionBuilder withAddress(String address) {
    try {
      this.address = new URL(address);
      return this;
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("the provided address is not a well formed URL: [" + address + "]", e);
    }
  }

  public WebServiceDefinitionBuilder withService(String service) {
    this.service = service;
    return this;
  }

  public WebServiceDefinitionBuilder withPort(String port) {
    this.port = port;
    return this;
  }

  public WebServiceDefinitionBuilder withExcludedOperations(List<String> excludedOperations) {
    this.excludedOperations = excludedOperations;
    return this;
  }

  public WebServiceDefinition build() {
    checkNotNull(serviceId, "ID cannot be null");
    checkNotNull(wsdlUrl, "WSDL Url cannot be null");
    checkNotNull(service, "Service cannot be null");
    checkNotNull(port, "Port Url cannot be null");
    return new WebServiceDefinition(serviceId, friendlyName, wsdlUrl, address, service, port, excludedOperations);
  }
}

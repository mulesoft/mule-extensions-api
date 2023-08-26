/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Representation of a web service, carrying the WSDL that can be accessible either locally or remotely, a Service, a Port. and an
 * ID to identify this web service so set of operations can be discovered for execution.
 * <p>
 * Optionally this definition can be populated with a friendly name, a list of excluded operations and an address since not all
 * retrieved services will hit the same endpoint, and for that reason each address will probably be different, If not address is
 * provided () the underlying Soap Service will try to find the one specified in the WSDL
 *
 * @since 1.0
 */
public final class WebServiceDefinition {

  private final String serviceId;
  private final String friendlyName;
  private final URL wsdlUrl;
  private final URL address;
  private final String service;
  private final String port;
  private final List<String> excludedOperationNames;

  WebServiceDefinition(String serviceId,
                       String friendlyName,
                       URL wsdlUrl,
                       URL address,
                       String service,
                       String port,
                       List<String> excludedOperations) {
    this.serviceId = serviceId;
    this.friendlyName = friendlyName;
    this.wsdlUrl = wsdlUrl;
    this.address = address;
    this.service = service;
    this.port = port;
    this.excludedOperationNames = excludedOperations;
  }

  public static WebServiceDefinitionBuilder builder() {
    return new WebServiceDefinitionBuilder();
  }

  /**
   * @return the display name the end user will see in UI.
   */
  public String getFriendlyName() {
    return friendlyName;
  }

  /**
   * @return URLs that targets the real WSDL file.
   */
  public URL getWsdlUrl() {
    return wsdlUrl;
  }

  /**
   * @return service's name of the given WSDL file, if empty will try to pick it automatically (if there is only one).
   */
  public String getService() {
    return service;
  }

  /**
   * @return port's name for a given service of the given WSDL. If empty will try to pick it automatically.
   */
  public String getPort() {
    return port;
  }

  /**
   * @return a List of operation names that should be excluded from the exposed service definition.
   */
  public List<String> getExcludedOperations() {
    return excludedOperationNames;
  }

  /**
   * @return an Id that univocally identifies the web service.
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * @return the address location for this web service.
   */
  public URL getAddress() {
    return address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WebServiceDefinition that = (WebServiceDefinition) o;
    return Objects.equals(serviceId, that.serviceId) &&
        Objects.equals(friendlyName, that.friendlyName) &&
        Objects.equals(wsdlUrl, that.wsdlUrl) &&
        Objects.equals(address, that.address) &&
        Objects.equals(service, that.service) &&
        Objects.equals(port, that.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceId, friendlyName, wsdlUrl, address, service, port);
  }
}

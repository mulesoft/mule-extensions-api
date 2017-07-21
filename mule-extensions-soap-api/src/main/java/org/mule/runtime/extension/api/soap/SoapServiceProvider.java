/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import org.mule.runtime.extension.api.soap.security.SecurityStrategy;
import java.util.List;
import java.util.Map;

/**
 * Contract for implementations that handles the web services that the extension will be able to execute by returning a list of
 * {@link WebServiceDefinition}s.
 * <p>
 * Implementations can also add a level of security by overriding the {@link SoapServiceProvider#getSecurities()}
 * method.
 *
 * @since 1.0
 */
public interface SoapServiceProvider {

  /**
   * @return a {@link List} with all the {@link WebServiceDefinition}s that the provider implementation handles.
   */
  List<WebServiceDefinition> getWebServiceDefinitions();

  /**
   * @return a {@link List} with all the Soap Securities that should be added for the services described by the provider impl.
   */
  default List<SecurityStrategy> getSecurities() {
    return emptyList();
  }

  /**
   * Gives the capability to create Soap headers given the a service definition and the operation that is being consumed
   * so they can be bundled with the soap request
   * <p>
   * It returns a {@link Map} with {@link String} keys representing the name of the headers and {@link String} values
   * representing the actual XML soap header value.
   *
   * @param definition the {@link WebServiceDefinition} of the service being called.
   * @param operation  the name of the operation that is going to be consumed.
   * @return a {@link Map} of soap headers to be bundled in the generated envelope.
   */
  default Map<String, String> getCustomHeaders(WebServiceDefinition definition, String operation) {
    return emptyMap();
  }

  /**
   * This method is a hook for {@link SoapServiceProvider} instances to validate the configured parameters and
   * fail gracefully before attempting to create a connection avoiding misleading and confusing error messages.
   *
   * @throws SoapServiceProviderConfigurationException if any configured parameter has an invalid value.
   */
  default void validateConfiguration() throws SoapServiceProviderConfigurationException {}
}

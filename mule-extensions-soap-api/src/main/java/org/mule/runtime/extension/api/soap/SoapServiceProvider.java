/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

import org.mule.runtime.extension.api.soap.message.MessageDispatcher;
import org.mule.runtime.extension.api.soap.security.SecurityStrategy;

import java.util.List;
import java.util.Optional;

/**
 * Contract for implementations that handles the web services that the extension will be able to execute by returning a list of
 * {@link WebServiceDefinition}s.
 * <p>
 * Implementations can also add a level of security by overriding the {@link SoapServiceProvider#getSecurities()}
 * method and on top of Soap Security the {@link SoapServiceProvider#getCustomDispatcher()} enables the capability to send
 * the soap messages using custom behaviour.
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
   * Gives the capability to create {@link SoapHeader}s given the a service definition and the operation that is being consumed
   * so they can be bundled with the soap request
   *
   * @param definition the {@link WebServiceDefinition} of the service being called.
   * @param operation  the name of the operation that is going to be consumed.
   * @return a {@link List} of {@link SoapHeader}s to be bundled in the generated envelope.
   */
  default List<SoapHeader> getCustomHeaders(WebServiceDefinition definition, String operation) {
    return emptyList();
  }

  /**
   * Allows the Soap Extension to dispatch the soap message using a custom {@link MessageDispatcher}. This makes possible for
   * example (among many use cases) to enable HTTP Basic Authentication or use another protocol that is not HTTP to
   * send the soap message.
   * <p>
   * If the returned object is {@link Optional#empty()} then the SDK will create a default {@link MessageDispatcher} to send the
   * soap messages over HTTP.
   *
   * @return an {@link Optional} {@link MessageDispatcher} to dispatch the soap messages in a custom way.
   */
  default Optional<MessageDispatcher> getCustomDispatcher() {
    return empty();
  }
}

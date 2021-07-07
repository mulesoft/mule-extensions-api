/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.security;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.security.Authentication;
import org.mule.runtime.api.security.Credentials;
import org.mule.runtime.api.security.CredentialsBuilder;
import org.mule.runtime.api.security.SecurityException;
import org.mule.runtime.api.security.SecurityProviderNotFoundException;
import org.mule.runtime.api.security.UnknownAuthenticationTypeException;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.List;
import java.util.Optional;

/**
 * This handler allows to configure the current context's authentication, used for encryption and inbound authentication, based on
 * a given {@link Authentication} request.
 *
 * @since 1.0
 */
@MinMuleVersion("4.0")
@NoImplement
public interface AuthenticationHandler {

  /**
   * Updates the current context with the given {@link Authentication} information.
   *
   * @param authentication the {@link Authentication} used for setting up the new security context
   *
   * @throws SecurityProviderNotFoundException  if no security provider able to authenticate the given authentication is found
   * @throws SecurityException                  if an error occurs during the update of the security context
   * @throws UnknownAuthenticationTypeException if no security provider able to handle the given authentication is found
   */
  void setAuthentication(Authentication authentication)
      throws SecurityProviderNotFoundException, SecurityException, UnknownAuthenticationTypeException;

  /**
   * Updates the current context with the given {@link Authentication} information.
   *
   * @param securityProviders the {@link List} of security providers that will be added to the {@code SecurityManager}
   * @param authentication    the {@link Authentication} used for setting up the new security context
   *
   * @throws SecurityProviderNotFoundException  if no security provider able to authenticate the given authentication is found
   * @throws SecurityException                  if an error occurs during the update of the security context
   * @throws UnknownAuthenticationTypeException if no security provider able to handle the given authentication is found
   */
  void setAuthentication(List<String> securityProviders, Authentication authentication)
      throws SecurityProviderNotFoundException, SecurityException, UnknownAuthenticationTypeException;

  /**
   * @return the {@link Authentication} in the current context
   */
  Optional<Authentication> getAuthentication();

  /**
   * @param credentials the {@link Credentials} to be used for {@code this} {@link Authentication}
   * @return a new instance of a default implementation of {@link Authentication}
   */
  Authentication createAuthentication(Credentials credentials);

  /**
   * @return a new {@link CredentialsBuilder}
   */
  CredentialsBuilder createCredentialsBuilder();

}

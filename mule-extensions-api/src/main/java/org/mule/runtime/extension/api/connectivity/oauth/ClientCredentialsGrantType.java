/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import static java.util.Optional.ofNullable;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.mule.runtime.extension.api.security.CredentialsPlacement.BASIC_AUTH_HEADER;

import org.mule.runtime.extension.api.security.CredentialsPlacement;

import java.util.Optional;

/**
 * Implementation of {@link OAuthGrantType} which contains information about how to use the Client Credentials grant type against
 * a particular OAuth provider
 *
 * @since 1.2.1
 */
public final class ClientCredentialsGrantType implements OAuthGrantType {

  public static final String NAME = "Client Credentials";

  private final String tokenUrl;
  private final String accessTokenExpr;
  private final String expirationRegex;
  private final String defaultScopes;
  private final CredentialsPlacement credentialsPlacement;

  /**
   * Creates a new instance
   *
   * @param tokenUrl        The url of the access token endpoint
   * @param accessTokenExpr Expression used to extract the access token from the {@code accessTokenUrl} response
   * @param expirationRegex Expression used to extract the expiration from the {@code accessTokenUrl} response
   * @param defaultScopes   The default scopes to be request
   */
  public ClientCredentialsGrantType(String tokenUrl,
                                    String accessTokenExpr,
                                    String expirationRegex,
                                    String defaultScopes,
                                    CredentialsPlacement credentialsPlacement) {

    notBlank(tokenUrl, "tokenUrl");
    notBlank(accessTokenExpr, "accessTokenExpr");
    notBlank(expirationRegex, "expirationRegex");
    notBlank(expirationRegex, "expirationRegex");

    this.tokenUrl = tokenUrl;
    this.accessTokenExpr = accessTokenExpr;
    this.expirationRegex = expirationRegex;
    this.defaultScopes = isBlank(defaultScopes) ? null : defaultScopes;
    this.credentialsPlacement = credentialsPlacement != null ? credentialsPlacement : BASIC_AUTH_HEADER;
  }

  @Override
  public void accept(OAuthGrantTypeVisitor visitor) {
    visitor.visit(this);
  }

  private void notBlank(String value, String name) {
    if (value == null || value.trim().length() == 0) {
      throw new IllegalArgumentException(name + " cannot be blank");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return The url of the access token endpoint
   */
  public String getTokenUrl() {
    return tokenUrl;
  }

  /**
   * @return Expression used to extract the access token from the {@code accessTokenUrl} response
   */
  @Override
  public String getAccessTokenExpr() {
    return accessTokenExpr;
  }

  /**
   * @return Expression used to extract the expiration from the {@code accessTokenUrl} response
   */
  @Override
  public String getExpirationRegex() {
    return expirationRegex;
  }

  /**
   * @return The default scopes to be requested
   */
  public Optional<String> getDefaultScopes() {
    return ofNullable(defaultScopes);
  }

  public CredentialsPlacement getCredentialsPlacement() {
    return credentialsPlacement;
  }
}

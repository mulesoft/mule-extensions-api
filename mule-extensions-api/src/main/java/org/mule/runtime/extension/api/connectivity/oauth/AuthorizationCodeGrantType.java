/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

/**
 * Implementaiton of {@Link OAuthGrantType} which contains inforamtion about how to use the
 * Authorization-Code grant type against a particular OAuth provider
 *
 * @since 1.0
 */
public final class AuthorizationCodeGrantType implements OAuthGrantType {

  public static final String NAME = "Authorization Code";

  private final String accessTokenUrl;
  private final String authorizationUrl;
  private final String accessTokenExpr;
  private final String expirationRegex;
  private final String refreshTokenExpr;
  private final String defaultScope;

  /**
   * Creates a new instance
   *
   * @param accessTokenUrl   The url of the access token endpoint
   * @param authorizationUrl The url of the authorization endpoint which initiates the dance
   * @param accessTokenExpr  Expression used to extract the access token from the {@code accessTokenUrl} response
   * @param expirationRegex  Expression used to extract the expiration from the {@code accessTokenUrl} response
   * @param refreshTokenExpr Expression used to extract the refresh token from the {@code accessTokenUrl} response
   * @param defaultScope     The default scopes to be request
   */
  public AuthorizationCodeGrantType(String accessTokenUrl, String authorizationUrl, String accessTokenExpr,
                                    String expirationRegex, String refreshTokenExpr, String defaultScope) {
    this.accessTokenUrl = accessTokenUrl;
    this.authorizationUrl = authorizationUrl;
    this.accessTokenExpr = accessTokenExpr;
    this.expirationRegex = expirationRegex;
    this.refreshTokenExpr = refreshTokenExpr;
    this.defaultScope = defaultScope;
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
  public String getAccessTokenUrl() {
    return accessTokenUrl;
  }

  /**
   * @return The url of the authorization endpoint which initiates the dance
   */
  public String getAuthorizationUrl() {
    return authorizationUrl;
  }

  /**
   * @return Expression used to extract the access token from the {@code accessTokenUrl} response
   */
  public String getAccessTokenExpr() {
    return accessTokenExpr;
  }

  /**
   * @return Expression used to extract the expiration from the {@code accessTokenUrl} response
   */
  public String getExpirationRegex() {
    return expirationRegex;
  }

  /**
   * @return Expression used to extract the refresh token from the {@code accessTokenUrl} response
   */
  public String getRefreshTokenExpr() {
    return refreshTokenExpr;
  }

  /**
   * @return The default scopes to be request
   */
  public String getDefaultScope() {
    return defaultScope;
  }
}

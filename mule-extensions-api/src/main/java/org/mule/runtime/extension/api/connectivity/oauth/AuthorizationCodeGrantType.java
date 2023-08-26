/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.mule.runtime.extension.api.security.CredentialsPlacement.BODY;

import org.mule.runtime.extension.api.security.CredentialsPlacement;

import java.util.Optional;

/**
 * Implementation of {@Link OAuthGrantType} which contains information about how to use the Authorization-Code grant type against
 * a particular OAuth provider
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
  private final CredentialsPlacement credentialsPlacement;
  private final boolean includeRedirectUriInRefreshTokenRequest;

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
    this(accessTokenUrl, authorizationUrl, accessTokenExpr, expirationRegex, refreshTokenExpr, defaultScope, null, true);
  }

  /**
   * Creates a new instance
   *
   * @param accessTokenUrl                          The url of the access token endpoint
   * @param authorizationUrl                        The url of the authorization endpoint which initiates the dance
   * @param accessTokenExpr                         Expression used to extract the access token from the {@code accessTokenUrl}
   *                                                response
   * @param expirationRegex                         Expression used to extract the expiration from the {@code accessTokenUrl}
   *                                                response
   * @param refreshTokenExpr                        Expression used to extract the refresh token from the {@code accessTokenUrl}
   *                                                response
   * @param defaultScope                            The default scopes to be request
   * @param credentialsPlacement                    The place where the credentials will be sent on the token request
   * @param includeRedirectUriInRefreshTokenRequest Indicates whether the redirect_uri parameter should be included in the refresh
   *                                                token request
   *
   * @since 1.4.0
   */
  public AuthorizationCodeGrantType(String accessTokenUrl, String authorizationUrl, String accessTokenExpr,
                                    String expirationRegex, String refreshTokenExpr, String defaultScope,
                                    CredentialsPlacement credentialsPlacement, boolean includeRedirectUriInRefreshTokenRequest) {
    notBlank(accessTokenUrl, "accessTokenUrl");
    notBlank(authorizationUrl, "authorizationUrl");
    notBlank(accessTokenExpr, "accessTokenExpr");
    notBlank(expirationRegex, "expirationRegex");
    notBlank(expirationRegex, "expirationRegex");

    this.accessTokenUrl = accessTokenUrl;
    this.authorizationUrl = authorizationUrl;
    this.accessTokenExpr = accessTokenExpr;
    this.expirationRegex = expirationRegex;
    this.refreshTokenExpr = refreshTokenExpr;
    this.defaultScope = isBlank(defaultScope) ? null : defaultScope;
    this.credentialsPlacement = credentialsPlacement != null ? credentialsPlacement : BODY;
    this.includeRedirectUriInRefreshTokenRequest = includeRedirectUriInRefreshTokenRequest;
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
   * @return Expression used to extract the refresh token from the {@code accessTokenUrl} response
   */
  public String getRefreshTokenExpr() {
    return refreshTokenExpr;
  }

  /**
   * @return The default scopes to be requested
   */
  public Optional<String> getDefaultScope() {
    return ofNullable(defaultScope);
  }

  /**
   * @return the place where credentials will be sent for the token request
   *
   * @since 1.4.0
   */
  public CredentialsPlacement getCredentialsPlacement() {
    return credentialsPlacement;
  }

  /**
   * @return whether the request_uri parameter should be included in the refresh token request
   *
   * @since 1.4.0
   */
  public boolean includeRedirectUriInRefreshTokenRequest() {
    return includeRedirectUriInRefreshTokenRequest;
  }
}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.NamedObject;

/**
 * Base contract for a Grant Type as defined in the OAuth2 specification.
 * <p>
 * Instances are to contain information about how to use this grant type against a particular OAuth provider.
 *
 * @since 1.0
 */
@NoImplement
public interface OAuthGrantType extends NamedObject {

  /**
   * @return The expression to be used to extract the access token from the service provider response
   * @since 1.3.0
   */
  String getAccessTokenExpr();

  /**
   * @return The regular expression to be used to extract the expiration time from the service provider response
   * @since 1.3.0
   */
  String getExpirationRegex();

  /**
   * Accepts a visitor
   *
   * @param visitor an {@link OAuthGrantTypeVisitor}
   * @since 1.2.1
   */
  void accept(OAuthGrantTypeVisitor visitor);
}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.Experimental;

/**
 * Implementation of the visitor pattern for {@link OAuthGrantType} implementations
 *
 * @since 1.2.1
 */
public interface OAuthGrantTypeVisitor {

  /**
   * visit a grant type for Authorization Code
   *
   * @param grantType the grant type
   */
  void visit(AuthorizationCodeGrantType grantType);

  /**
   * visit a grant type for Client Credentials
   *
   * @param grantType the grant type
   */
  void visit(ClientCredentialsGrantType grantType);

  /**
   * visit a grant type for Platform Managed Connections
   * <p>
   * Platform Managed OAuth is an experimental feature. It will only be enabled on selected environments and scenarios. Backwards
   * compatibility is not guaranteed.
   *
   * @param grantType the grant type
   * @since 1.3.0
   */
  @Experimental
  void visit(PlatformManagedOAuthGrantType grantType);
}

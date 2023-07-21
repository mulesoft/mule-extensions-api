/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.Experimental;

/**
 * A grant type that represent a Platform Managed OAuth connections. This grant type could be consider synthetic as this doesn't
 * exist in the OAuth specification.
 * <p>
 * Platform Managed OAuth is an experimental feature. It will only be enabled on selected environments and scenarios. Backwards
 * compatibility is not guaranteed.
 *
 * @since 1.3.0
 */
@Experimental
public final class PlatformManagedOAuthGrantType implements OAuthGrantType {

  public static final String NAME = "Platform Managed";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getAccessTokenExpr() {
    return "#[payload.access_token]";
  }

  @Override
  public String getExpirationRegex() {
    return "#[payload.expires_in]";
  }

  @Override
  public void accept(OAuthGrantTypeVisitor visitor) {
    visitor.visit(this);
  }
}

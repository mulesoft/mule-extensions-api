/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

public class PlatformManagedOAuthGrantType implements OAuthGrantType {

  public static final String NAME = "Platform Managed";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void accept(OAuthGrantTypeVisitor visitor) {
    visitor.visit(this);
  }
}

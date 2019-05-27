/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

/**
 * Implementation of the visitor pattern for {@link OAuthGrantType} implemenetations
 *
 * @since 1.2.1
 */
public interface OAuthGrantTypeVisitor {


  void visit(AuthorizationCodeGrantType grantType);

  void visit(ClientCredentialsGrantType grantType);
}

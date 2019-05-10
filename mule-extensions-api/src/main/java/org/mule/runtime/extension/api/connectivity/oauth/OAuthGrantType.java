/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.NamedObject;

/**
 * Base contract for a Grant Type as defined in the OAuth2 specification.
 * <p>
 * Instances are to contain information about how to use this grant type against
 * a particular OAuth provider.
 *
 * @since 1.0
 */
@NoImplement
public interface OAuthGrantType extends NamedObject {

  void accept(OAuthGrantTypeVisitor visitor);
}

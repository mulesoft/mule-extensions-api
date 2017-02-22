/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.OAuthParameter;

/**
 * A private {@link ModelProperty} used on {@link ParameterModel} instances, indicating that such
 * parameter should be used as an OAuth parameter.
 *
 * This is equivalent to the {@link OAuthParameter} annotation
 */
public class OAuthParameterModelProperty implements ModelProperty {

  private final String requestAlias;

  /**
   * Creates a new instance
   * @param requestAlias the name under which the parameter is sent to the OAuth provider during the authentication dance
   */
  public OAuthParameterModelProperty(String requestAlias) {
    this.requestAlias = requestAlias;
  }

  /**
   * @return the name under which the parameter is sent to the OAuth provider during the authentication dance
   */
  public String getRequestAlias() {
    return requestAlias;
  }

  /**
   * {@inheritDoc}
   * @return {@code oauthParameter}
   */
  @Override
  public String getName() {
    return "oauthParameter";
  }

  /**
   * {@inheritDoc}
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}

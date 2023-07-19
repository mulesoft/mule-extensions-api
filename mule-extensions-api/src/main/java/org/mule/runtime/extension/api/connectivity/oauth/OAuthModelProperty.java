/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import static java.util.Collections.unmodifiableList;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.meta.model.ModelProperty;

import java.util.List;

/**
 * A public {@link ModelProperty} used to indicate that a {@link ConnectionProvider} supports a number of OAuth2 grant types
 *
 * @since 1.0
 */
public class OAuthModelProperty implements ModelProperty {

  public static final String NAME = "OAuth";
  private final List<OAuthGrantType> grantTypes;

  /**
   * Creates a new instance
   * 
   * @param grantTypes the {@link OAuthGrantType grant types} supported
   */
  public OAuthModelProperty(List<OAuthGrantType> grantTypes) {
    this.grantTypes = unmodifiableList(grantTypes);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code oauth}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  /**
   * @return the supported grant types
   */
  public List<OAuthGrantType> getGrantTypes() {
    return grantTypes;
  }
}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security.config;

/**
 * {@link WssStoreConfiguration} implementation for Trust Stores, used for signature verification.
 *
 * @since 1.0
 */
public final class WssTrustStoreConfiguration implements WssStoreConfiguration {

  private String trustStorePath;
  private String password;
  private String type;

  public WssTrustStoreConfiguration(String trustStorePath, String password, String type) {
    this.trustStorePath = trustStorePath;
    this.password = password;
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStorePath() {
    return trustStorePath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getType() {
    return type;
  }
}

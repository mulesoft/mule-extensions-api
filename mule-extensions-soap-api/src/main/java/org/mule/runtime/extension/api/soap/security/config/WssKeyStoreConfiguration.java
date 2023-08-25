/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security.config;

/**
 * {@link WssStoreConfiguration} implementation for Key Stores, used for encryption, decryption and signing.
 *
 * @since 1.0
 */
public final class WssKeyStoreConfiguration implements WssStoreConfiguration {

  private final String alias;
  private final String keyPassword;
  private final String password;
  private final String keyStorePath;
  private final String type;

  public WssKeyStoreConfiguration(String alias, String keyPassword, String password, String keyStorePath, String type) {
    this.alias = alias;
    this.keyPassword = keyPassword;
    this.password = password;
    this.keyStorePath = keyStorePath;
    this.type = type;
  }

  public WssKeyStoreConfiguration(String alias, String password, String keyStorePath) {
    this(alias, null, password, keyStorePath, "jks");
  }

  /**
   * @return The password used to access the private key.
   */
  public String getKeyPassword() {
    return keyPassword;
  }

  /**
   * @return The alias of the private key to use.
   */
  public String getAlias() {
    return alias;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStorePath() {
    return keyStorePath;
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

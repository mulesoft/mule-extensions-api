/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;


import org.mule.runtime.extension.api.soap.security.config.WssKeyStoreConfiguration;

/**
 * Verifies the signature of a SOAP response, using certificates of the trust-store in the provided TLS context.
 *
 * @since 1.0
 */
public final class EncryptSecurityStrategy implements SecurityStrategy {

  /**
   * The keystore to use when encrypting the message.
   */
  private final WssKeyStoreConfiguration keyStoreConfiguration;

  public EncryptSecurityStrategy(WssKeyStoreConfiguration keyStoreConfiguration) {
    this.keyStoreConfiguration = keyStoreConfiguration;
  }

  public WssKeyStoreConfiguration getKeyStoreConfiguration() {
    return keyStoreConfiguration;
  }

  @Override
  public void accept(SecurityStrategyVisitor visitor) {
    visitor.visitEncrypt(this);
  }
}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;


import org.mule.runtime.extension.api.soap.security.config.WssKeyStoreConfiguration;

/**
 * Decrypts an encrypted SOAP response, using the private key of the key-store in the provided TLS context.
 *
 * @since 1.0
 */
public final class DecryptSecurityStrategy implements SecurityStrategy {

  /**
   * The keystore to use when decrypting the message.
   */
  private final WssKeyStoreConfiguration keyStoreConfiguration;

  public DecryptSecurityStrategy(WssKeyStoreConfiguration keyStoreConfiguration) {
    this.keyStoreConfiguration = keyStoreConfiguration;
  }

  public WssKeyStoreConfiguration getKeyStoreConfiguration() {
    return keyStoreConfiguration;
  }

  @Override
  public void accept(SecurityStrategyVisitor visitor) {
    visitor.visitDecrypt(this);
  }
}

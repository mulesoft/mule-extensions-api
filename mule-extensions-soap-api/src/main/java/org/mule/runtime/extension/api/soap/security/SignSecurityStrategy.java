/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;

import org.mule.runtime.extension.api.soap.security.config.WssKeyStoreConfiguration;

/**
 * Signs the SOAP request that is being sent, using the private key of the key-store in the provided TLS context.
 *
 * @since 1.0
 */
public final class SignSecurityStrategy implements SecurityStrategy {

  /**
   * The keystore to use when signing the message.
   */
  private final WssKeyStoreConfiguration keyStoreConfiguration;

  public SignSecurityStrategy(WssKeyStoreConfiguration keyStoreConfiguration) {
    this.keyStoreConfiguration = keyStoreConfiguration;
  }

  public WssKeyStoreConfiguration getKeyStoreConfiguration() {
    return keyStoreConfiguration;
  }

  @Override
  public void accept(SecurityStrategyVisitor visitor) {
    visitor.visitSign(this);
  }
}

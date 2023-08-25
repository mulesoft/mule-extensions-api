/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;


import static java.util.Optional.ofNullable;
import org.mule.runtime.extension.api.soap.security.config.WssTrustStoreConfiguration;

import java.util.Optional;


/**
 * Verifies the signature of a SOAP response, using certificates of the trust-store in the provided TLS context.
 *
 * @since 1.0
 */
public final class VerifySignatureSecurityStrategy implements SecurityStrategy {

  /**
   * The truststore to use to verify the signature.
   */
  private final WssTrustStoreConfiguration trustStoreConfiguration;

  public VerifySignatureSecurityStrategy(WssTrustStoreConfiguration trustStoreConfiguration) {
    this.trustStoreConfiguration = trustStoreConfiguration;
  }

  public VerifySignatureSecurityStrategy() {
    this.trustStoreConfiguration = null;
  }

  public Optional<WssTrustStoreConfiguration> getTrustStoreConfiguration() {
    return ofNullable(trustStoreConfiguration);
  }

  @Override
  public void accept(SecurityStrategyVisitor visitor) {
    visitor.visitVerify(this);
  }
}

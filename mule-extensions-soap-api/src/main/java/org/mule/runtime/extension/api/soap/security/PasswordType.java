/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;


/**
 * Password types that can be configured in a {@link SecurityStrategy}.
 *
 * @since 1.0
 */
public enum PasswordType {

  /**
   * The actual password for the username, the password hash, or derived password.
   */
  TEXT("PasswordText"),

  /**
   * The digest of the password (and optionally nonce and/or creation timestamp) for the username.
   */
  DIGEST("PasswordDigest");

  private final String type;

  PasswordType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}

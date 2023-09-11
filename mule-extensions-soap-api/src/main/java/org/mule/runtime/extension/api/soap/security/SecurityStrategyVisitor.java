/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.security;

/**
 * Used in {@link SecurityStrategy#accept(SecurityStrategyVisitor)} as a visitor pattern.
 *
 * @since 1.0
 */
public interface SecurityStrategyVisitor {

  void visitEncrypt(EncryptSecurityStrategy encrypt);

  void visitDecrypt(DecryptSecurityStrategy decrypt);

  void visitUsernameToken(UsernameTokenSecurityStrategy usernameToken);

  void visitSign(SignSecurityStrategy sign);

  void visitVerify(VerifySignatureSecurityStrategy verify);

  void visitTimestamp(TimestampSecurityStrategy timestamp);
}

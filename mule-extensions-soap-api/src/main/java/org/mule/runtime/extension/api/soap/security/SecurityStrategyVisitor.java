/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.soap.security;

/**
 * Contract for objects that adds a level of security to the SOAP Protocol.
 *
 * @since 1.0
 */
public interface SecurityStrategy {

  /**
   * Dispatches in a reflective way to the method with the specific {@link SecurityStrategy} type as argument.
   *
   * @param visitor the accepted visitor.
   */
  void accept(SecurityStrategyVisitor visitor);

}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.grammar;

import org.antlr.runtime.Token;

/**
 * Represents the OR token in a Dsql Query.
 *
 * @since 1.0
 */
final class OrDsqlNode extends BaseDsqlNode {

  OrDsqlNode(Token t) {
    super(t);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(DsqlGrammarVisitor visitor) {
    visitor.visit(this);
  }
}

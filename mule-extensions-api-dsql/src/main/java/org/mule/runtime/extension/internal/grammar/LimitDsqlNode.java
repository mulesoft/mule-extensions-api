/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.antlr.runtime.Token;

/**
 * Represents the LIMIT token in a Dsql Query.
 *
 * @since 1.0
 */
final class LimitDsqlNode extends BaseDsqlNode {

  LimitDsqlNode(Token t) {
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

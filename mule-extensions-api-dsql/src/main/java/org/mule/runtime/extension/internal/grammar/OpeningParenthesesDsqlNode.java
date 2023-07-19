/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.antlr.runtime.Token;

/**
 * Represents the OPEN PARENTHESES token in a Dsql Query.
 *
 * @since 1.0
 */
final class OpeningParenthesesDsqlNode extends BaseDsqlNode {

  OpeningParenthesesDsqlNode(Token t) {
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

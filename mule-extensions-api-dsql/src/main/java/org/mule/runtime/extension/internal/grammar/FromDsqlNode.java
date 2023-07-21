/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.antlr.runtime.Token;

/**
 * Represents the FROM token in a Dsql Query.
 *
 * @since 1.0
 */
final class FromDsqlNode extends BaseDsqlNode {

  FromDsqlNode(Token t) {
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

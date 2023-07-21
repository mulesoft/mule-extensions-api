/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.mule.runtime.extension.internal.exception.DsqlParsingException;

import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonErrorNode;

/**
 * A node representing erroneous token range in the DSQL token stream.
 *
 * @since 1.0
 */
class DsqlErrorNode extends CommonErrorNode implements DsqlNode {

  DsqlErrorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
    super(input, start, stop, e);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(DsqlGrammarVisitor visitor) {
    throw new DsqlParsingException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<DsqlNode> getChildren() {
    return (List<DsqlNode>) super.getChildren();
  }

}

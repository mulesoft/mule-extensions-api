/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import java.util.List;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 * Base implementation for a {@link DsqlNode}.
 *
 * @since 1.0
 */
public class BaseDsqlNode extends CommonTree implements DsqlNode {

  public BaseDsqlNode(Token token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(DsqlGrammarVisitor visitor) {
    visitor.visit(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DsqlNode> getChildren() {
    return (List<DsqlNode>) super.getChildren();
  }
}

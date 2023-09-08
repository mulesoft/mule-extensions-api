/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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

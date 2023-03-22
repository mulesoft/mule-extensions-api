/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.grammar;

import org.mule.runtime.extension.api.dsql.DsqlQuery;

import java.util.List;

/**
 * Generic contract for all the nodes that can be part of a {@link DefaultDsqlQuery}.
 *
 * @since 1.0
 */
interface DsqlNode {

  /**
   * Returns the node type needed for tree parsing.
   *
   * @return an {@code int} value representing the type of the node.
   */
  int getType();

  /**
   * @return a {@link List} with all the childs {@link DsqlNode}s of {@code this} current node.
   */
  List<DsqlNode> getChildren();

  /**
   * Return the node value as a {@link String}
   *
   * @return a {@link String} instance, representing the value of the node.
   */
  String getText();

  /**
   * Parses {@code this} node into a {@link DefaultDsqlQuery} element, using the provided
   * {@link DsqlGrammarVisitor}.
   *
   * @param visitor an {@link DsqlGrammarVisitor} instance.
   */
  void accept(DsqlGrammarVisitor visitor);

}

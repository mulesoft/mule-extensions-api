/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.grammar;

import org.mule.runtime.extension.api.dsql.DsqlQuery;

/**
 * Generic contract for a visitor that goes over a DSQL Grammar and creates
 * and generates a {@link DsqlQuery}
 *
 * @since 1.0
 */
public interface DsqlGrammarVisitor {

  void visit(BaseDsqlNode baseDsqlNode);

  void visit(SelectDsqlNode selectDsqlNode);

  void visit(FromDsqlNode fromDsqlNode);

  void visit(ExpressionDsqlNode selectDsqlNode);

  void visit(AndDsqlNode andDsqlNode);

  void visit(OrDsqlNode orDsqlNode);

  void visit(NotDsqlNode notDsqlNode);

  void visit(OpeningParenthesesDsqlNode openingParenthesesDsqlNode);

  void visit(OperatorDsqlNode operatorDsqlNode);

  void visit(OrderByDsqlNode orderByDsqlNode);

  void visit(LimitDsqlNode limitDsqlNode);

  void visit(OffsetDsqlNode offsetDsqlNode);
}

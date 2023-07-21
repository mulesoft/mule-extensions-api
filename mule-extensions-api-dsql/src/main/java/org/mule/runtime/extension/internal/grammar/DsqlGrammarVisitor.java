/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;

import org.mule.runtime.extension.api.dsql.DsqlQuery;

/**
 * Generic contract for a visitor that goes over a DSQL Grammar and creates and generates a {@link DsqlQuery}
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

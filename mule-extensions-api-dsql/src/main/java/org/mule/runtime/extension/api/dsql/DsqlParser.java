/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import org.mule.runtime.extension.internal.MuleDsqlLexer;
import org.mule.runtime.extension.internal.MuleDsqlParser;
import org.mule.runtime.extension.internal.exception.DsqlParsingException;
import org.mule.runtime.extension.internal.grammar.DefaultDsqlGrammarVisitor;
import org.mule.runtime.extension.internal.grammar.BaseDsqlNode;
import org.mule.runtime.extension.internal.grammar.DsqlTreeAdaptor;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

/**
 * This class works as a processor of {@link DsqlQuery}s, conforming to the rules of the formal defined grammar.
 * <p>
 * Basically receives a Dsql Query as a {@link String} instance and process it to return a {@link DsqlQuery} object
 * to work with.
 *
 * @since 1.0
 */
public final class DsqlParser {

  /**
   * A prefix that marks that a query is a {@link DsqlQuery}
   */
  private static final String DSQL_QUERY_PREFIX = "dsql:";

  private DsqlParser() {}

  /**
   * @return a new {@link DsqlParser} instance.
   */
  public static DsqlParser getInstance() {
    return new DsqlParser();
  }

  /**
   * Checks if a {@link String} that represents a query is a {@link DsqlQuery} or
   * another kind of query (Native Query).
   *
   * @param query a {@link String} representing a query.
   * @return true if the query is a dsql query, false otherwise.
   */
  public static boolean isDsqlQuery(String query) {
    return query.substring(0, 5).toLowerCase().equals(DSQL_QUERY_PREFIX);
  }

  /**
   * Parses a {@link String} representing a dsql query into a proper {@link DsqlQuery} instance.
   *
   * @param dsqlQuery a {@link String} that represents a {@link DsqlQuery}.
   * @return a parsed {@link DsqlQuery} instance.
   */
  public DsqlQuery parse(final String dsqlQuery) {

    if (!isDsqlQuery(dsqlQuery)) {
      throw new IllegalArgumentException("Invalid Query: DSQL queries must start with the [dsql:] prefix");
    }

    MuleDsqlLexer dsqlLexer = new MuleDsqlLexer(new ANTLRStringStream(dsqlQuery.substring(5)));

    CommonTokenStream dsqlTokens = new CommonTokenStream();
    dsqlTokens.setTokenSource(dsqlLexer);

    MuleDsqlParser dsqlParser = new MuleDsqlParser(dsqlTokens);
    dsqlParser.setTreeAdaptor(new DsqlTreeAdaptor());

    return parse(dsqlParser);
  }

  private DsqlQuery parse(MuleDsqlParser dsqlParser) {
    try {
      MuleDsqlParser.select_return select = dsqlParser.select();
      BaseDsqlNode tree = (BaseDsqlNode) select.getTree();
      return buildQuery(tree).build();
    } catch (RecognitionException | IllegalArgumentException e) {
      throw new DsqlParsingException(e);
    }
  }

  private QueryBuilder buildQuery(BaseDsqlNode dsqlRootNode) {
    DefaultDsqlGrammarVisitor visitor = new DefaultDsqlGrammarVisitor();
    dsqlRootNode.accept(visitor);
    return visitor.getQueryBuilder();
  }

}

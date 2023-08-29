/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import java.util.ServiceLoader;

/**
 * This class works as a processor of {@link DsqlQuery}s, conforming to the rules of the formal defined grammar.
 * <p>
 * Basically receives a Dsql Query as a {@link String} instance and process it to return a {@link DsqlQuery} object to work with.
 *
 * @since 1.0
 */
public abstract class DsqlParser {

  /**
   * A prefix that marks that a query is a {@link DsqlQuery}
   */
  private static final String DSQL_QUERY_PREFIX = "dsql:";

  /**
   * @return a new {@link DsqlParser} instance.
   */
  public static DsqlParser getInstance() {
    return ServiceLoader.load(DsqlParser.class, DsqlParser.class.getClassLoader()).iterator().next();
  }

  /**
   * Checks if a {@link String} that represents a query is a {@link DsqlQuery} or another kind of query (Native Query).
   *
   * @param query a {@link String} representing a query.
   * @return true if the query is a dsql query, false otherwise.
   */
  public static boolean isDsqlQuery(String query) {
    return query.length() > 5 && query.toLowerCase().startsWith(DSQL_QUERY_PREFIX);
  }

  /**
   * Parses a {@link String} representing a dsql query into a proper {@link DsqlQuery} instance.
   *
   * @param dsqlQuery a {@link String} that represents a {@link DsqlQuery}.
   * @return a parsed {@link DsqlQuery} instance.
   */
  public abstract DsqlQuery parse(final String dsqlQuery);

}

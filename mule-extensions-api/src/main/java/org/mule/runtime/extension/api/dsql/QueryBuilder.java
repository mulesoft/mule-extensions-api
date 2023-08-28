/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

/**
 * Builder interface to create basic query objects.
 *
 * @since 1.0
 */
public interface QueryBuilder {

  /**
   * Sets the queried entity exposed by the application.
   *
   * @param type the queried entity type.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder setType(EntityType type);

  /**
   * Sets one of the selected fields from the queried entity.
   *
   * @param field a selected field from the queried entity
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder addField(Field field);

  /**
   * Sets one of the fields to order by from.
   *
   * @param field a field used to sort the query.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder addOrderByField(Field field);

  /**
   * Sets the sorting direction of the query.
   *
   * @param direction the sorting direction, ASCENDING or DESCENDING.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder setDirection(Direction direction);

  /**
   * Sets an expression to filter the returned query elements.
   *
   * @param expression a filter expression.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder setFilterExpression(Expression expression);

  /**
   * Sets the maximum results that can be retrieved by the builded query.
   *
   * @param limit the number of max results that can be retrieved by the query.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder setLimit(int limit);

  /**
   * Sets the number of items to skip from the beginning of the result set.
   *
   * @param offset the number of items to skip from the beginning of the result.
   * @return {@code this} {@link QueryBuilder} instance.
   */
  QueryBuilder setOffset(int offset);

  /**
   * Returns the final builded {@link DsqlQuery}.
   *
   * @return a {@link DsqlQuery} instance.
   */
  DsqlQuery build();

}

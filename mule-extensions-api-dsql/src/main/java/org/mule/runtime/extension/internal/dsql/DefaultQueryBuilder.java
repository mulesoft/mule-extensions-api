/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsql;


import org.mule.runtime.extension.api.dsql.Direction;
import org.mule.runtime.extension.api.dsql.EntityType;
import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.Field;
import org.mule.runtime.extension.api.dsql.QueryBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Builder pattern implementation to build {@link DefaultDsqlQuery}s incrementally.
 * <p>
 * Intended to be used by Studio/Mozart.
 *
 * @since 1.0
 */
public class DefaultQueryBuilder implements QueryBuilder {

  /**
   * Type or types to be queried
   */
  private EntityType type;

  /**
   * Fields to be retrieved
   */
  private final List<Field> fields = new LinkedList<>();

  /**
   * Fields for sorting the query
   */
  private final List<Field> orderByFields = new LinkedList<>();

  /**
   * Direction to determine the ascending or descending sorting
   */
  private Direction direction;

  /**
   * Expression which contains the filter conditions
   */
  private Expression filterExpression;

  /**
   * The maximum number of items to return in the result set.
   */
  private Integer limit;

  /**
   * The number of items to skip from the beginning of the result set.
   */
  private Integer offset;

  /**
   * Hide constructor.
   */
  private DefaultQueryBuilder() {}

  /**
   * @return a new {@link DefaultQueryBuilder} instance.
   */
  public static DefaultQueryBuilder getInstance() {
    return new DefaultQueryBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder setType(EntityType type) {
    this.type = type;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder addField(Field field) {
    this.fields.add(field);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder addOrderByField(Field field) {
    this.orderByFields.add(field);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder setDirection(Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder setFilterExpression(Expression expression) {
    this.filterExpression = expression;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder setLimit(int limit) {
    this.limit = limit;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public QueryBuilder setOffset(int offset) {
    this.offset = offset;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultDsqlQuery build() {
    if (type == null) {
      throw new IllegalArgumentException("Cannot build query. Missing queried entity type");
    }

    if (fields.isEmpty()) {
      fields.add(new Field("*"));
    }

    return new DefaultDsqlQuery(type, fields, orderByFields, direction, filterExpression, limit, offset);
  }
}

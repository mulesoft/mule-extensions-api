/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import static java.util.Collections.unmodifiableList;
import org.mule.runtime.extension.api.introspection.dsql.Direction;
import org.mule.runtime.extension.api.introspection.dsql.EntityType;
import org.mule.runtime.extension.api.introspection.dsql.Field;
import org.mule.runtime.extension.api.introspection.dsql.QueryTranslator;
import org.mule.runtime.extension.internal.expression.EmptyExpression;
import org.mule.runtime.extension.internal.expression.Expression;

import java.util.List;


/**
 * Represents a parsed DSQL query.
 *
 * @since 1.0
 */
public final class DsqlQuery {

  /**
   * Type or types to be queried
   */
  private EntityType type;

  /**
   * Fields to be retrieved
   */
  private List<Field> fields;

  /**
   * Fields for sorting the query
   */
  private List<Field> orderByFields;

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
   * Creates a new instance.
   *
   * @param type the entity type to be queried
   * @param fields the selected fields of the entity.
   * @param orderByFields the fields to order the query
   * @param direction ascending or descending sort order.
   * @param filterExpression an expression to filter the result set items.
   * @param limit the maximum number of items to be returned.
   * @param offset the number of items to skip from the beginning of the result set.
   */
  public DsqlQuery(EntityType type,
                   List<Field> fields,
                   List<Field> orderByFields,
                   Direction direction,
                   Expression filterExpression,
                   Integer limit,
                   Integer offset) {
    this.type = type;
    this.fields = fields;
    this.orderByFields = orderByFields;
    this.direction = direction == null ? Direction.ASC : direction;
    this.filterExpression = filterExpression == null ? new EmptyExpression() : filterExpression;
    this.limit = limit;
    this.offset = offset;
  }

  /**
   * @return the selected fields from the queried entity.
   */
  public List<Field> getFields() {
    return unmodifiableList(fields);
  }

  /**
   * @return the fields used to sort the result set.
   */
  public List<Field> getOrderByFields() {
    return unmodifiableList(orderByFields);
  }

  /**
   * @return the sorting direction.
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * @return an {@link Expression} to filter the queried result set.
   */
  public Expression getFilterExpression() {
    return filterExpression;
  }

  /**
   * @return the entity that is being queried.
   */
  public EntityType getType() {
    return type;
  }

  /**
   * @return the maximum number of item returned in the result set.
   */
  public int getLimit() {
    return limit;
  }

  /**
   * @return the number of items to skip from the beginning of the result set.
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Translates {@code this} {@link DsqlQuery} to a query in another query language using the specified
   * {@code queryTranslator}.
   *
   * @param queryTranslator a {@link QueryTranslator} instance used to translate from DSQL to another query language.
   * @return a {@link String} that represents a translated query using the {@code queryTranslator}.
   */
  public String translate(QueryTranslator queryTranslator) {
    queryTranslator.translateFields(fields);
    queryTranslator.translateTypes(type);
    if (!filterExpression.isEmpty()) {
      queryTranslator.translateBeginExpression();
      filterExpression.accept(queryTranslator);
    }

    if (orderByFields.size() > 0) {
      queryTranslator.translateOrderByFields(orderByFields, direction);
    }

    if (limit != null) {
      queryTranslator.translateLimit(limit);
    }

    if (offset != null) {
      queryTranslator.translateOffset(offset);
    }

    return queryTranslator.getTranslation();
  }

}

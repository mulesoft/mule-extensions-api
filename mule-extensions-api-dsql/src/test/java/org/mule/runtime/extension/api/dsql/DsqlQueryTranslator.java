/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import java.util.Iterator;
import java.util.List;

/**
 * An {@link QueryTranslator} implementation that translates an DSQL query represented as
 * {@link DsqlQuery} object to its {@link String} representation.
 *
 * @since 1.0
 */
public class DsqlQueryTranslator implements QueryTranslator {

  private StringBuilder queryBuilder;

  DsqlQueryTranslator() {
    queryBuilder = new StringBuilder();
  }

  @Override
  public void translateFields(List<Field> fields) {
    StringBuilder select = new StringBuilder();
    select.append("SELECT ");
    Iterator<Field> fieldIterable = fields.iterator();
    while (fieldIterable.hasNext()) {
      String fieldName = addQuotesIfNeeded(fieldIterable.next().getName());
      select.append(fieldName);
      if (fieldIterable.hasNext()) {
        select.append(",");
      }
    }

    queryBuilder.insert(0, select);
  }

  @Override
  public void translateTypes(EntityType type) {
    queryBuilder.append(" FROM ");
    String typeName = addQuotesIfNeeded(type.getName());
    queryBuilder.append(typeName);
  }

  @Override
  public void translateOrderByFields(List<Field> orderByFields, Direction direction) {
    queryBuilder.append(" ORDER BY ");
    Iterator<Field> orderByFieldsIterator = orderByFields.iterator();
    while (orderByFieldsIterator.hasNext()) {
      String fieldName = addQuotesIfNeeded(orderByFieldsIterator.next().getName());
      queryBuilder.append(fieldName);
      if (orderByFieldsIterator.hasNext()) {
        queryBuilder.append(",");
      }
    }

    queryBuilder.append(" ");
    queryBuilder.append(direction.toString());
  }

  @Override
  public void translateBeginExpression() {
    queryBuilder.append(" WHERE ");
  }

  @Override
  public void translateInitPrecedence() {
    queryBuilder.append("(");
  }

  @Override
  public void translateEndPrecedence() {
    queryBuilder.append(")");
  }

  @Override
  public void translateLimit(int limit) {
    queryBuilder.append(" LIMIT ").append(limit);
  }

  @Override
  public void translateOffset(int offset) {
    queryBuilder.append(" OFFSET ").append(offset);
  }

  @Override
  public void translateAnd() {
    queryBuilder.append(" AND ");
  }

  @Override
  public void translateOR() {
    queryBuilder.append(" OR ");
  }

  @Override
  public void translateComparison(String operator, Field field, Value<?> value) {
    String name = addQuotesIfNeeded(field.getName());
    queryBuilder.append(name).append(operator).append(value.toString());
  }

  @Override
  public OperatorTranslator operatorTranslator() {
    return new DefaultOperatorTranslator();
  }

  @Override
  public String getTranslation() {
    return queryBuilder.toString();
  }

  private String addQuotesIfNeeded(String name) {
    return name.contains(" ") ? "'" + name + "'" : name;
  }

}

/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

import java.util.List;


/**
 * Represents a parsed DSQL query.
 *
 * @since 1.0
 */
public interface DsqlQuery {

  /**
   * @return the selected fields from the queried entity.
   */
  public List<Field> getFields();

  /**
   * @return the fields used to sort the result set.
   */
  public List<Field> getOrderByFields();

  /**
   * @return the sorting direction.
   */
  public Direction getDirection();

  /**
   * @return an {@link Expression} to filter the queried result set.
   */
  public Expression getFilterExpression();

  /**
   * @return the entity that is being queried.
   */
  public EntityType getType();

  /**
   * @return the maximum number of item returned in the result set.
   */
  public int getLimit();

  /**
   * @return the number of items to skip from the beginning of the result set.
   */
  public int getOffset();

  /**
   * Translates {@code this} {@link DsqlQuery} to a query in another query language using the specified {@code queryTranslator}.
   *
   * @param queryTranslator a {@link QueryTranslator} instance used to translate from DSQL to another query language.
   * @return a {@link String} that represents a translated query using the {@code queryTranslator}.
   */
  public String translate(QueryTranslator queryTranslator);

}

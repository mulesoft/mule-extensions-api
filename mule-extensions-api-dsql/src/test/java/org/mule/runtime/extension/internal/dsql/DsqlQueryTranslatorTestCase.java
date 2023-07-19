/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.dsql;

import static org.mule.runtime.extension.internal.value.IdentifierValue.fromLiteral;

import static org.junit.Assert.assertEquals;

import org.mule.runtime.extension.api.dsql.Direction;
import org.mule.runtime.extension.api.dsql.EntityType;
import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.api.dsql.Field;
import org.mule.runtime.extension.api.dsql.QueryBuilder;
import org.mule.runtime.extension.internal.dsql.DefaultQueryBuilder;
import org.mule.runtime.extension.internal.expression.And;
import org.mule.runtime.extension.internal.expression.FieldComparison;
import org.mule.runtime.extension.internal.expression.Or;
import org.mule.runtime.extension.internal.operator.EqualsOperator;
import org.mule.runtime.extension.internal.operator.GreaterOperator;
import org.mule.runtime.extension.internal.operator.LessOperator;
import org.mule.runtime.extension.internal.operator.NotEqualsOperator;
import org.mule.runtime.extension.internal.value.IntegerValue;
import org.mule.runtime.extension.internal.value.NumberValue;
import org.mule.runtime.extension.internal.value.StringValue;

import org.junit.Test;

public class DsqlQueryTranslatorTestCase {

  @Test
  public void testBasicQueryVisitor() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.addField(new Field("lastName", "string"));
    queryBuilder.setType(new EntityType("Account"));

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);
    assertEquals("SELECT name,lastName FROM Account", visitor.getTranslation());
  }

  @Test
  public void testFiltersQueryVisitor() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
    Expression anotherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
    Expression simpleAnd = new And(comparision, anotherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);
    assertEquals("SELECT name FROM Account WHERE (age < 18 AND grade > 0)", visitor.getTranslation());

  }


  @Test
  public void testFiltersQueryVisitorWithDouble() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new NumberValue(18.5));
    Expression anotherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), new NumberValue(0.5));
    Expression simpleAnd = new And(comparision, anotherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);
    assertEquals("SELECT name FROM Account WHERE (age < 18.5 AND grade > 0.5)", visitor.getTranslation());

  }

  @Test
  public void testPrecedence() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    Expression comparision = new FieldComparison(new NotEqualsOperator(), new Field("age", "int"), new IntegerValue(18));
    Expression anotherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
    Expression simpleOr = new Or(comparision, anotherComparision);
    Expression simpleAnd = new And(simpleOr, anotherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);
    assertEquals("SELECT name FROM Account WHERE ((age <> 18 OR grade > 0) AND grade > 0)", visitor.getTranslation());
  }

  @Test
  public void testOrderBy() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    queryBuilder.addOrderByField(new Field("name", "string"));
    queryBuilder.addOrderByField(new Field("age", "int"));
    queryBuilder.setDirection(Direction.ASC);
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
    Expression anotherComparision = new FieldComparison(new EqualsOperator(), new Field("grade", "int"), new IntegerValue(0));
    Expression simpleOr = new Or(comparision, anotherComparision);
    Expression simpleAnd = new And(simpleOr, anotherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);

    String select = "SELECT name FROM Account WHERE ((age < 18 OR grade = 0) AND grade = 0) ORDER BY name,age ASC";
    assertEquals(select, visitor.getTranslation());
  }

  @Test
  public void testLimitAndOffset() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    queryBuilder.addOrderByField(new Field("name", "string"));
    queryBuilder.addOrderByField(new Field("age", "int"));
    queryBuilder.setDirection(Direction.DESC);
    queryBuilder.setLimit(10);
    queryBuilder.setOffset(20);
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
    Expression anotherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), new IntegerValue(0));
    Expression simpleOr = new Or(comparision, anotherComparision);
    Expression simpleAnd = new And(simpleOr, anotherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);

    String select = "SELECT name FROM Account WHERE ((age < 18 OR grade > 0) AND grade > 0) " +
        "ORDER BY name,age DESC LIMIT 10 OFFSET 20";
    assertEquals(select, visitor.getTranslation());
  }

  @Test
  public void testIdentifierValue() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    queryBuilder.addOrderByField(new Field("name", "string"));
    queryBuilder.addOrderByField(new Field("age", "int"));
    queryBuilder.setDirection(Direction.ASC);
    queryBuilder.setLimit(10);
    queryBuilder.setOffset(20);
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new IntegerValue(18));
    Expression otherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), fromLiteral("NEXT_WEEK"));
    Expression simpleOr = new Or(comparision, otherComparision);
    Expression simpleAnd = new And(simpleOr, otherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);

    String select = "SELECT name FROM Account WHERE ((age < 18 OR grade > NEXT_WEEK) AND grade > NEXT_WEEK) " +
        "ORDER BY name,age ASC LIMIT 10 OFFSET 20";

    assertEquals(select, visitor.getTranslation());
  }


  @Test
  public void testStringValue() {
    QueryBuilder queryBuilder = DefaultQueryBuilder.getInstance();
    queryBuilder.addField(new Field("name", "string"));
    queryBuilder.setType(new EntityType("Account"));
    queryBuilder.addOrderByField(new Field("name", "string"));
    queryBuilder.addOrderByField(new Field("age", "int"));
    queryBuilder.setDirection(Direction.ASC);
    queryBuilder.setLimit(10);
    queryBuilder.setOffset(20);
    Expression comparision = new FieldComparison(new LessOperator(), new Field("age", "int"), new StringValue("old"));
    Expression otherComparision = new FieldComparison(new GreaterOperator(), new Field("grade", "int"), fromLiteral("NEXT_WEEK"));
    Expression simpleOr = new Or(comparision, otherComparision);
    Expression simpleAnd = new And(simpleOr, otherComparision);
    queryBuilder.setFilterExpression(simpleAnd);

    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    queryBuilder.build().translate(visitor);

    String select = "SELECT name FROM Account WHERE ((age < 'old' OR grade > NEXT_WEEK) AND grade > NEXT_WEEK) " +
        "ORDER BY name,age ASC LIMIT 10 OFFSET 20";
    assertEquals(select, visitor.getTranslation());
  }
}

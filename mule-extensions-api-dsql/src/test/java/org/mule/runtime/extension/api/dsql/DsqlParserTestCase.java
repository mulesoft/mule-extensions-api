/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.mule.runtime.extension.internal.operator.EqualsOperator;
import org.mule.runtime.extension.internal.expression.FieldComparison;
import org.mule.runtime.extension.internal.operator.LessOperator;

import org.junit.Test;

/**
 *
 */
public class DsqlParserTestCase {

  private static final DsqlParser PARSER = DsqlParser.getInstance();

  @Test
  public void testEmptyParse() {
    try {
      DsqlQuery dsqlQuery = PARSER.parse("dsql:select * from users");
      assertEquals("*", dsqlQuery.getFields().get(0).getName());
      assertEquals("users", dsqlQuery.getType().getName());
    } catch (Throwable e) {
      fail();
    }
  }

  @Test
  public void testWithMuleExpressionDsql() {
    DsqlQuery dsqlQuery = PARSER.parse("dsql:select * from addresses where name='#[payload.get(\\'id\\')]' order by name desc");
    assertEquals("*", dsqlQuery.getFields().get(0).getName());
    assertEquals("addresses", dsqlQuery.getType().getName());
    assertTrue(dsqlQuery.getFilterExpression() instanceof FieldComparison);
    assertEquals("name", ((FieldComparison) dsqlQuery.getFilterExpression()).getField().getName());
    assertTrue(((FieldComparison) dsqlQuery.getFilterExpression()).getOperator() instanceof EqualsOperator);
    assertEquals("'#[payload.get(\\'id\\')]'", ((FieldComparison) dsqlQuery.getFilterExpression()).getValue().toString());
    assertEquals("name", dsqlQuery.getOrderByFields().get(0).getName());
  }

  @Test
  public void testWithMuleExpression() {
    DsqlQuery dsqlQuery =
        PARSER.parse("dsql:SELECT * from addresses where name='#[payload.get(\\'id\\')]' order by name desc");
    assertEquals("*", dsqlQuery.getFields().get(0).getName());
    assertEquals("addresses", dsqlQuery.getType().getName());
    assertTrue(dsqlQuery.getFilterExpression() instanceof FieldComparison);
    assertEquals("name", ((FieldComparison) dsqlQuery.getFilterExpression()).getField().getName());
    assertTrue(((FieldComparison) dsqlQuery.getFilterExpression()).getOperator() instanceof EqualsOperator);
    assertEquals("'#[payload.get(\\'id\\')]'", ((FieldComparison) dsqlQuery.getFilterExpression()).getValue().toString());
    assertEquals("name", dsqlQuery.getOrderByFields().get(0).getName());
  }

  @Test
  public void testWithMuleExpressionFlowVarDsql() {
    DsqlQuery dsqlQuery =
        PARSER.parse("dsql:select id,name from addresses where name<'#[flowVars[\"id\"]]' order by name desc");
    assertEquals(2, dsqlQuery.getFields().size());
    assertEquals("id", dsqlQuery.getFields().get(0).getName());
    assertEquals("name", dsqlQuery.getFields().get(1).getName());
    assertEquals("addresses", dsqlQuery.getType().getName());
    assertTrue(dsqlQuery.getFilterExpression() instanceof FieldComparison);
    assertEquals("name", ((FieldComparison) dsqlQuery.getFilterExpression()).getField().getName());
    assertTrue(((FieldComparison) dsqlQuery.getFilterExpression()).getOperator() instanceof LessOperator);
    assertEquals("'#[flowVars[\"id\"]]'", ((FieldComparison) dsqlQuery.getFilterExpression()).getValue().toString());
    assertEquals("name", dsqlQuery.getOrderByFields().get(0).getName());
    assertEquals(Direction.DESC, dsqlQuery.getDirection());
  }

  @Test
  public void testWithMuleExpressionFlowVar() {
    DsqlQuery dsqlQuery = PARSER.parse("dsql:SELECT id,name from addresses where name<'#[flowVars[\"id\"]]' order by name desc");
    assertEquals(2, dsqlQuery.getFields().size());
    assertEquals("id", dsqlQuery.getFields().get(0).getName());
    assertEquals("name", dsqlQuery.getFields().get(1).getName());
    assertEquals("addresses", dsqlQuery.getType().getName());
    assertTrue(dsqlQuery.getFilterExpression() instanceof FieldComparison);
    assertEquals("name", ((FieldComparison) dsqlQuery.getFilterExpression()).getField().getName());
    assertTrue(((FieldComparison) dsqlQuery.getFilterExpression()).getOperator() instanceof LessOperator);
    assertEquals("'#[flowVars[\"id\"]]'", ((FieldComparison) dsqlQuery.getFilterExpression()).getValue().toString());
    assertEquals("name", dsqlQuery.getOrderByFields().get(0).getName());
  }
}

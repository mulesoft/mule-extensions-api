/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

import org.mule.runtime.extension.api.dsql.Direction;
import org.mule.runtime.extension.api.dsql.DsqlParser;
import org.mule.runtime.extension.api.dsql.DsqlQuery;
import org.mule.runtime.extension.api.dsql.Expression;
import org.mule.runtime.extension.internal.exception.DsqlParsingException;
import org.mule.runtime.extension.internal.expression.BinaryLogicalExpression;
import org.mule.runtime.extension.internal.expression.FieldComparison;
import org.mule.runtime.extension.internal.operator.EqualsOperator;
import org.mule.runtime.extension.internal.operator.LessOperator;
import org.mule.runtime.extension.internal.operator.Operator;
import org.mule.runtime.extension.internal.value.IntegerValue;
import org.mule.runtime.extension.internal.value.NumberValue;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import org.junit.Assert;
import org.junit.Test;

import org.hamcrest.CoreMatchers;

public class MuleDsqlParserTestCase {

  // Debug toggle to show ASTs being parsed by the tests
  private static final boolean PRINT_AST = false;

  @Test
  public void testEasyParse() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users where name='alejo'");
    assertThat(dsqlQuery.getFields().size(), is(1));
    assertThat(dsqlQuery.getFields().get(0).getName(), is("*"));
    assertThat(dsqlQuery.getType().getName(), is("users"));
    Expression filterExpression = dsqlQuery.getFilterExpression();
    assertFieldComparation(filterExpression, EqualsOperator.class, "name", "alejo");
  }

  @SuppressWarnings("unchecked")
  private void assertFieldComparation(Expression filterExpression, Class<? extends Operator> operatorClass, String fieldName,
                                      Object value) {
    assertThat((FieldComparison) filterExpression, isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) filterExpression;
    assertThat(fieldComparison.getField().getName(), is(fieldName));
    assertThat(fieldComparison.getValue().getValue(), is(value));
    Class<Operator> operator = (Class<Operator>) operatorClass;
    assertThat(fieldComparison.getOperator(), isA(operator));
  }

  @Test
  public void testParse1() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT name, surname from addresses where name='alejo' and (apellido='abdala' and address='guatemala 1234') order by name limit 10 offset 200");

    assertThat(dsqlQuery.getFields().size(), is(2));
    assertThat(dsqlQuery.getFields().get(0).getName(), is("name"));
    assertThat(dsqlQuery.getFields().get(1).getName(), is("surname"));

    assertThat(dsqlQuery.getType().getName(), is("addresses"));

    assertThat(dsqlQuery.getOrderByFields().size(), is(1));
    assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("name"));

    assertThat((BinaryLogicalExpression) dsqlQuery.getFilterExpression(), isA(BinaryLogicalExpression.class));
    BinaryLogicalExpression andExpression = (BinaryLogicalExpression) dsqlQuery.getFilterExpression();
    assertFieldComparation(andExpression.getLeft(), EqualsOperator.class, "name", "alejo");
    assertThat((BinaryLogicalExpression) andExpression.getRight(), isA(BinaryLogicalExpression.class));

    BinaryLogicalExpression innerAnd = (BinaryLogicalExpression) andExpression.getRight();
    assertFieldComparation(innerAnd.getLeft(), EqualsOperator.class, "apellido", "abdala");
    assertFieldComparation(innerAnd.getRight(), EqualsOperator.class, "address", "guatemala 1234");

  }


  @Test
  public void whenNumberLiteralNumberDoesNotContainsFloatingPointItShouldBeSetAsLong() {
    DsqlQuery parse = parse("dsql:SELECT name from account where age > 20");
    Expression filterExpression = parse.getFilterExpression();
    assertThat(filterExpression, CoreMatchers.instanceOf(FieldComparison.class));
    assertThat(((FieldComparison) filterExpression).getValue(), CoreMatchers.instanceOf(IntegerValue.class));
  }

  @Test
  public void whenNumberLiteralNumberContainsFloatingPointItShouldBeSetAsDouble() {
    DsqlQuery parse = parse("dsql:SELECT name from account where age > 20.3");
    Expression filterExpression = parse.getFilterExpression();
    assertThat(filterExpression, CoreMatchers.instanceOf(FieldComparison.class));
    assertThat(((FieldComparison) filterExpression).getValue(), CoreMatchers.instanceOf(NumberValue.class));
  }

  @Test
  public void testParse1b() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT name, surname from users, addresses where (name='alejo' and apellido='abdala') and address='guatemala 1234' order by name desc limit 10 offset 200");
    Assert.assertThat(dsqlQuery.getFields().size(), is(2));
  }

  @Test
  public void testParse2() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT * from users, addresses where name='alejo' and apellido='abdala' or apellido='achaval' and name='mariano' and cp='1234'");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParse3() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' and not (age > 25)");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testLike() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name like '%alejo%'");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testExpression() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT AccountNumber,AccountSource,Active__c FROM Account WHERE AccountNumber = '#[flowVars[\\'name\\']]'");
    Assert.assertThat(dsqlQuery.getFields().size(), is(3));
  }

  @Test
  public void testParse4() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' and age <> 25");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParse5() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' and (age >= 25 or age <= 40)");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParse6() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT AccountSource,AnnualRevenue FROM Account WHERE ((AnnualRevenue > 22222 AND BillingCity > 123) AND AnnualRevenue >= 222222) ORDER BY Active__c LIMIT 112 OFFSET 222");
    Assert.assertThat(dsqlQuery.getFields().size(), is(2));
  }

  @Test
  public void testParseAscending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' order by name ascending");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParseAscending2() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' order by name asc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParseDescending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' order by name descending");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testParseDescending2() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='alejo' order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='#[payload.name]' order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression2() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='#[payload.get(\\'id\\')]' order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithEscapedIdentifiers() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT [select], [desc] from [from] where [where] = 2 order by [asc] asc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(2));
  }


  @Test
  public void testWithMuleExpression3() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='#[flowVars[\"id\"]]' order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression4() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where id > #[flowVars['pepe']] order by name");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression5() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT * from users, addresses where (id > #[flowVars['pepe']] and id < #[flowVars.get('id')]) order by name");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression6() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT * from users, addresses where id > #[flowVars['pepe']] and id < #[flowVars.get('id')] order by name");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression7() {
    DsqlQuery dsqlQuery =
        parse("dsql:SELECT * from users, addresses where id > #[flowVars['pepe']] and id < #[[flowVars.get('[id')]] order by name");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpression9() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name='#[flowVars[\\'id\\']]' order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpressionWithDoubleQuotes() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name=\"#[flowVars['id']]\" order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test
  public void testWithMuleExpressionWithEscapedDoubleQuotesInString() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT * from users, addresses where name=\"#[flowVars[\\\"id\\\"]]\" order by name desc");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));
  }

  @Test(expected = DsqlParsingException.class)
  public void testFail() {
    parse("dsql:SELECT * from users, addresses where name='alejo' and ");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFail2() {
    parse("dsql:select from");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFail3() {
    parse("dsql:*");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFail4() {
    parse("dsql:SELECT *");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFail5() {
    parse("dsql:SELECT * from users, addresses where ");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailSelect() {
    parse("dsql:selecct users, addresses from Account where name = 123");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailFrom() {
    parse("dsql:SELECT users, addresses frrom Account where name = 123");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailFrom2() {
    parse("dsql:SELECT users, addresses *");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailFrom3() {
    parse("dsql:SELECT users, addresses ffrom");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailMissingFrom() {
    parse("dsql:SELECT users, addresses where");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailWhere2() {
    parse("dsql:SELECT users, addresses from Account where *");
  }

  @Test(expected = DsqlParsingException.class)
  public void testWithMuleExpressionShouldFail() {
    parse("dsql:SELECT * from users, addresses where name='#[flowVars[\'id\']]' order by name desc");
  }

  @Test(expected = DsqlParsingException.class)
  public void testFailWhere() {
    parse("dsql:SELECT users, addresses from Account whseree name = 123");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailNoPrefix() {
    parse("SELECT users, addresses from Account whseree name = 123");
  }

  @Test
  public void testFieldsWithSpaces() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT 'Field with spaces' from Account");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("Field with spaces"));
  }

  @Test
  public void testNormalFieldsMixedWithFieldsWithSpaces() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField,'Field with spaces',Underscored_Field from Account");
    Assert.assertThat(dsqlQuery.getFields().size(), is(3));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getFields().get(1).getName(), is("Field with spaces"));
    Assert.assertThat(dsqlQuery.getFields().get(2).getName(), is("Underscored_Field"));
  }

  @Test
  public void testNormalFieldWithQuotes() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT 'NormalField' from Account");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
  }

  @Test
  public void testNormalFieldWithQuotesMixedWithFieldsWithSpaces() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT 'NormalField','Field With Spaces' from Account");
    Assert.assertThat(dsqlQuery.getFields().size(), is(2));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getFields().get(1).getName(), is("Field With Spaces"));
  }

  @Test
  public void testTypeWithSpacesInFrom() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from 'Account Reps'");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getType().getName(), is("Account Reps"));
  }

  @Test
  public void testTypeWithSpacesMixedWithNormalInFrom() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from NormalType");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getType().getName(), is("NormalType"));
  }

  @Test
  public void testNormalTypeWithQuotes() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from 'NormalType'");
    Assert.assertThat(dsqlQuery.getFields().size(), is(1));

    Assert.assertThat(dsqlQuery.getFields().get(0).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getType().getName(), is("NormalType"));
  }

  @Test
  public void testTypeWithSpacesInOrderBy() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces'");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(1));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
  }

  @Test
  public void testTypeWithSpacesMixedWithNormalInOrderBy() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces',NormalField");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(2));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
    Assert.assertThat(dsqlQuery.getOrderByFields().get(1).getName(), is("NormalField"));
  }

  @Test
  public void testTypeWithSpacesInOrderByAscending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces' asc");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(1));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
    Assert.assertThat(dsqlQuery.getDirection(), is(Direction.ASC));
  }

  @Test
  public void testTypeWithSpacesMixedWithNormalInOrderByAscending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces',NormalField ascending");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(2));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
    Assert.assertThat(dsqlQuery.getOrderByFields().get(1).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getDirection(), is(Direction.ASC));
  }

  @Test
  public void testTypeWithSpacesInOrderByDescending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces' desc");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(1));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
    Assert.assertThat(dsqlQuery.getDirection(), is(Direction.DESC));
  }

  @Test
  public void testTypeWithSpacesMixedWithNormalInOrderByDescending() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account ORDER BY 'Field With Spaces',NormalField descending");
    Assert.assertThat(dsqlQuery.getOrderByFields().size(), is(2));


    Assert.assertThat(dsqlQuery.getOrderByFields().get(0).getName(), is("Field With Spaces"));
    Assert.assertThat(dsqlQuery.getOrderByFields().get(1).getName(), is("NormalField"));
    Assert.assertThat(dsqlQuery.getDirection(), is(Direction.DESC));
  }

  @Test
  public void testTypeWithSpacesInFilters() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account WHERE 'Field With Spaces' = 1");
    assertFieldComparation(dsqlQuery.getFilterExpression(), EqualsOperator.class, "Field With Spaces", 1);
  }

  @Test
  public void testFieldWithSpacesInFiltersWithParenthesis() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account WHERE ('Field With Spaces' = 1)");
    assertFieldComparation(dsqlQuery.getFilterExpression(), EqualsOperator.class, "Field With Spaces", 1);
  }

  @Test
  public void testFieldWithSpacesInComparison() {
    DsqlQuery dsqlQuery = parse("dsql:SELECT NormalField from Account WHERE ('Field With Spaces' < 1)");
    assertFieldComparation(dsqlQuery.getFilterExpression(), LessOperator.class, "Field With Spaces", 1);
  }

  public DsqlQuery parse(final String string) {
    if (PRINT_AST) {
      try {
        CharStream antlrStringStream = new ANTLRStringStream(string);
        MuleDsqlLexer dsqlLexer = new MuleDsqlLexer(antlrStringStream);
        CommonTokenStream dsqlTokens = new CommonTokenStream();
        dsqlTokens.setTokenSource(dsqlLexer);

        MuleDsqlParser dsqlParser = new MuleDsqlParser(dsqlTokens);
        MuleDsqlParser.select_return select = dsqlParser.select();

        CommonTree tree = select.getTree();
        printTree(tree);

      } catch (RecognitionException e) {
        throw new DsqlParsingException(e);
      }
    }
    DsqlParser parser = DsqlParser.getInstance();
    DsqlQueryTranslator visitor = new DsqlQueryTranslator();
    DsqlQuery parse = parser.parse(string);
    parse.translate(visitor);
    return parse;
  }

  private void printTree(CommonTree tree) {
    printTree(tree, 0);
  }

  @SuppressWarnings("unchecked")
  private void printTree(CommonTree tree, int level) {
    List<CommonTree> children = (List<CommonTree>) tree.getChildren();
    System.out.println(tree.getText() + " - Type=" + tree.getType());
    if (children != null) {
      level += 2;
      for (CommonTree t : children) {
        if (t != null) {
          printLevel(level);
          printTree(t, level);
        }
      }
    }
  }

  private void printLevel(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("-");
    }
    System.out.print("-> ");
  }
}

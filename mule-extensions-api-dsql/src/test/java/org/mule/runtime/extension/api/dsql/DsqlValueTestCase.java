/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
/**
 *
 */
package org.mule.runtime.extension.api.dsql;


import org.mule.runtime.extension.internal.value.BooleanValue;
import org.mule.runtime.extension.internal.value.DateTimeValue;
import org.mule.runtime.extension.internal.value.DateValue;
import org.mule.runtime.extension.internal.expression.FieldComparison;
import org.mule.runtime.extension.internal.value.IdentifierValue;
import org.mule.runtime.extension.internal.value.IntegerValue;
import org.mule.runtime.extension.internal.value.MuleExpressionValue;
import org.mule.runtime.extension.internal.value.NullValue;
import org.mule.runtime.extension.internal.value.StringValue;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class DsqlValueTestCase {

  private static final DsqlParser DSQL_PARSER = DsqlParser.getInstance();

  @Test
  public void parseStringValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where lastName = 'de Achaval'");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat((StringValue) fieldComparison.getValue(), CoreMatchers.isA(StringValue.class));
    StringValue stringValue = (StringValue) fieldComparison.getValue();

    Assert.assertThat(stringValue.getValue(), CoreMatchers.is("de Achaval"));
  }

  @Test
  public void parseDateTimeWithTimZone1Value() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birdth > 1999-01-01T23:01:01+01:00");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(DateTimeValue.class));
  }

  @Test
  public void parseDateTimeWithTimZone2Value() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birdth > 1999-01-01T23:01:01-01:00");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(DateTimeValue.class));
  }

  @Test
  public void parseDateTimeWithTimZone3Value() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birth > 1999-01-01T23:01:01Z");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(DateTimeValue.class));
  }


  @Test
  public void parseDateTimeWithOutTimZoneValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birth > 2013-09-05T16:39:26.621-03:00");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(DateTimeValue.class));
  }

  @Test
  public void parseDateValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birth > 1999-01-01");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(DateValue.class));
  }


  @Test
  public void parseIntValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where age = 30");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat(fieldComparison.getValue(), CoreMatchers.instanceOf(IntegerValue.class));
  }

  @Test
  public void parseBooleanValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where registered = true");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat((BooleanValue) fieldComparison.getValue(), CoreMatchers.isA(BooleanValue.class));
  }

  @Test
  public void parseMuleExpressionValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where address = #[flowVars['address']]");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat((MuleExpressionValue) fieldComparison.getValue(), CoreMatchers.isA(MuleExpressionValue.class));
    MuleExpressionValue stringValue = (MuleExpressionValue) fieldComparison.getValue();

    Assert.assertThat(stringValue.getValue(), CoreMatchers.is("#[flowVars['address']]"));
  }


  @Test
  public void parseIdentifierValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where birth > NEXT_WEEK");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat((IdentifierValue) fieldComparison.getValue(), CoreMatchers.isA(IdentifierValue.class));
    IdentifierValue stringValue = (IdentifierValue) fieldComparison.getValue();
    Assert.assertThat(stringValue.getValue(), CoreMatchers.is("NEXT_WEEK"));
  }


  @Test
  public void parseNullValue() {
    DsqlQuery parse = DSQL_PARSER.parse("dsql:SELECT name from account where address = null");
    Assert.assertThat((FieldComparison) parse.getFilterExpression(), CoreMatchers.isA(FieldComparison.class));
    FieldComparison fieldComparison = (FieldComparison) parse.getFilterExpression();
    Assert.assertThat((NullValue) fieldComparison.getValue(), CoreMatchers.isA(NullValue.class));
  }

}

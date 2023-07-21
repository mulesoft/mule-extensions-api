/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.dsql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.mule.runtime.extension.internal.operator.EqualsOperator;
import org.mule.runtime.extension.internal.operator.GreaterOperator;
import org.mule.runtime.extension.internal.operator.GreaterOrEqualsOperator;
import org.mule.runtime.extension.internal.operator.LessOperator;
import org.mule.runtime.extension.internal.operator.LikeOperator;

import org.junit.Test;

public class OperatorsTestCase {

  @Test
  public void testEquals() {
    assertThat(new EqualsOperator(), equalTo(new EqualsOperator()));
    assertThat(new LessOperator(), equalTo(new LessOperator()));
    assertThat(new GreaterOperator(), equalTo(new GreaterOperator()));
    assertThat(new LikeOperator(), equalTo(new LikeOperator()));
  }

  @Test
  public void testNotEquals() {
    assertThat(new EqualsOperator(), is(not(equalTo(new LessOperator()))));
    assertThat(new LessOperator(), is(not(equalTo(new GreaterOperator()))));
    assertThat(new LikeOperator(), is(not(equalTo(new GreaterOrEqualsOperator()))));
    assertThat(new EqualsOperator(), is(not(equalTo(new LikeOperator()))));
  }

}

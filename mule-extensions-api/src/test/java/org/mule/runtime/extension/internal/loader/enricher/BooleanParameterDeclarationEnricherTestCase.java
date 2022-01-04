/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BooleanParameterDeclarationEnricherTestCase {

  @Mock
  private ExtensionDeclarer declarer;

  @Mock
  private ExtensionDeclaration declaration;

  @Mock
  private ExtensionLoadingContext context;

  @Mock
  private OperationDeclaration operation;

  private ParameterDeclaration parameter;

  private BooleanParameterDeclarationEnricher enricher = new BooleanParameterDeclarationEnricher();

  @Before
  public void setUp() {
    when(context.getExtensionDeclarer()).thenReturn(declarer);
    when(declarer.getDeclaration()).thenReturn(declaration);
    when(declaration.getOperations()).thenReturn(asList(operation));

    parameter = new ParameterDeclaration("bool");
    parameter.setRequired(true);
    parameter.setType(create(JAVA).booleanType().build(), false);
    parameter.setExpressionSupport(SUPPORTED);

    ParameterGroupDeclaration group = mock(ParameterGroupDeclaration.class);
    when(group.getParameters()).thenReturn(asList(parameter));
    when(operation.getParameterGroups()).thenReturn(asList(group));
  }

  @Test
  public void booleanBecomesOptional() {
    enricher.enrich(context);
    assertThat(parameter.isRequired(), is(false));
    assertThat(parameter.getDefaultValue(), equalTo("false"));
  }

  @Test
  public void expressionBooleanRemainsUntouched() {
    parameter.setExpressionSupport(REQUIRED);
    enricher.enrich(context);
    assertThat(parameter.isRequired(), is(true));
    assertThat(parameter.getDefaultValue(), is(nullValue()));
  }

  @Test
  public void configOverride() {
    parameter.setConfigOverride(true);
    enricher.enrich(context);

    assertThat(parameter.isRequired(), is(false));
    assertThat(parameter.getDefaultValue(), is(nullValue()));
  }

  @Test
  public void honourExistingDefaultValue() {
    parameter.setRequired(false);
    parameter.setDefaultValue("true");

    enricher.enrich(context);
    assertThat(parameter.isRequired(), is(false));
    assertThat(parameter.getDefaultValue(), equalTo("true"));
  }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.internal.loader.enricher.BooleanParameterDeclarationEnricher.DONT_SET_DEFAULT_VALUE_TO_BOOLEAN_PARAMS;

import static java.util.Arrays.asList;
import static java.util.Optional.of;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.BooleanParameterDeclarationEnricher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.qameta.allure.Issue;

public class BooleanParameterDeclarationEnricherTestCase {

  @Rule
  public MockitoRule mockitorule = MockitoJUnit.rule();

  @Mock
  private ExtensionDeclarer declarer;

  @Mock
  private ExtensionDeclaration declaration;

  @Mock
  private ExtensionLoadingContext context;

  @Mock
  private OperationDeclaration operation;

  private ParameterDeclaration parameter;

  private final BooleanParameterDeclarationEnricher enricher = new BooleanParameterDeclarationEnricher();

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

  @Test
  @Issue("W-12003688")
  public void dontSetDefaultValueToBooleanParams() {
    when(context.getParameter(DONT_SET_DEFAULT_VALUE_TO_BOOLEAN_PARAMS)).thenReturn(of(true));
    enricher.enrich(context);

    assertThat(parameter.isRequired(), is(true));
    assertThat(parameter.getDefaultValue(), is(nullValue()));
  }
}

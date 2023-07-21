/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.BACK_PRESSURE_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.property.BackPressureStrategyModelProperty.getDefault;
import static org.mule.runtime.extension.api.runtime.source.BackPressureMode.DROP;
import static org.mule.runtime.extension.api.runtime.source.BackPressureMode.WAIT;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.BackPressureStrategyModelProperty;
import org.mule.runtime.extension.internal.loader.enricher.BackPressureDeclarationEnricher;

import java.util.LinkedHashSet;

import org.junit.Before;
import org.junit.Test;

public class BackPressureDeclarationEnricherTestCase {

  private BackPressureDeclarationEnricher backPressureDeclarationEnricher;
  private ExtensionLoadingContext extensionLoadingContext;
  private ExtensionDeclaration extensionDeclaration;
  private SourceDeclaration sourceDeclaration;
  private ParameterGroupDeclaration parameterGroupDeclaration;

  @Before
  public void before() {
    backPressureDeclarationEnricher = new BackPressureDeclarationEnricher();
    extensionLoadingContext = mock(ExtensionLoadingContext.class);

    extensionDeclaration = mock(ExtensionDeclaration.class);
    sourceDeclaration = mock(SourceDeclaration.class);

    final ExtensionDeclarer extensionDeclarer = mock(ExtensionDeclarer.class);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
    when(extensionDeclaration.getMessageSources()).thenReturn(asList(sourceDeclaration));
    parameterGroupDeclaration = new ParameterGroupDeclaration(DEFAULT_GROUP_NAME);

    when(sourceDeclaration.getParameterGroup(DEFAULT_GROUP_NAME)).thenReturn(parameterGroupDeclaration);
  }

  @Test
  public void modelPropertyIsNotPresent() {
    when(sourceDeclaration.getModelProperty(BackPressureStrategyModelProperty.class)).thenReturn(empty());
    backPressureDeclarationEnricher.enrich(extensionLoadingContext);
    assertThat(parameterGroupDeclaration.getParameters(), hasSize(0));
  }

  @Test
  public void enricherAddsParameter() {
    BackPressureStrategyModelProperty modelProperty =
        new BackPressureStrategyModelProperty(DROP, new LinkedHashSet<>(asList(DROP, WAIT)));
    when(sourceDeclaration.getModelProperty(BackPressureStrategyModelProperty.class)).thenReturn(of(modelProperty));
    backPressureDeclarationEnricher.enrich(extensionLoadingContext);
    assertThat(parameterGroupDeclaration.getParameters(), hasSize(1));
    ParameterDeclaration parameterDeclaration = parameterGroupDeclaration.getParameters().get(0);
    assertThat(parameterDeclaration.getName(), is(BACK_PRESSURE_STRATEGY_PARAMETER_NAME));
    assertThat(parameterDeclaration.getDefaultValue(), is(DROP));
  }

  @Test
  public void enricherDoesNotAddParameter() {
    when(sourceDeclaration.getModelProperty(BackPressureStrategyModelProperty.class)).thenReturn(of(getDefault()));
    backPressureDeclarationEnricher.enrich(extensionLoadingContext);
    assertThat(parameterGroupDeclaration.getParameters(), hasSize(0));
  }

}

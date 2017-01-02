/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.fluent.util;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithSourcesDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeclarationWalkerTestCase {

  @Mock
  private ExtensionDeclaration extension;

  @Mock
  private ConfigurationDeclaration configuration;

  @Mock
  private OperationDeclaration operation;

  @Mock
  private ConnectionProviderDeclaration connectionProvider;

  @Mock
  private ParameterGroupDeclaration parameterGroup;

  @Mock
  private ParameterDeclaration parameter;

  @Mock
  private SourceDeclaration source;

  @Before
  public void before() {
    when(extension.getConfigurations()).thenReturn(asList(configuration));
    when(extension.getOperations()).thenReturn(asList(operation));
    when(extension.getMessageSources()).thenReturn(asList(source));
    when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(configuration.getOperations()).thenReturn(asList(operation));
    when(configuration.getMessageSources()).thenReturn(asList(source));
    when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));
    when(parameterGroup.getParameters()).thenReturn(asList(parameter));

    addParameter(configuration, operation, connectionProvider, source);
  }

  private void addParameter(ParameterizedDeclaration... declarations) {
    for (ParameterizedDeclaration declaration : declarations) {
      when(declaration.getParameterGroups()).thenReturn(asList(parameterGroup));
    }
  }

  @Test
  public void walk() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 2);
    assertCount(sources, 2);
    assertCount(providers, 2);
    assertCount(parameterGroups, 7);
    assertCount(parameters, 7);
  }

  @Test
  public void stopOnConfig() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
        stop();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 0);
    assertCount(sources, 0);
    assertCount(providers, 0);
    assertCount(parameterGroups, 0);
    assertCount(parameters, 0);
  }

  @Test
  public void stopOnOperation() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
        stop();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 1);
    assertCount(sources, 1);
    assertCount(providers, 1);
    assertCount(parameterGroups, 3);
    assertCount(parameters, 3);
  }

  @Test
  public void stopOnProvider() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
        stop();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 0);
    assertCount(sources, 0);
    assertCount(providers, 1);
    assertCount(parameterGroups, 0);
    assertCount(parameters, 0);
  }

  @Test
  public void stopOnSource() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
        stop();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 0);
    assertCount(sources, 1);
    assertCount(providers, 1);
    assertCount(parameterGroups, 2);
    assertCount(parameters, 2);
  }

  @Test
  public void stopOnGroup() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
        stop();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 0);
    assertCount(sources, 0);
    assertCount(providers, 1);
    assertCount(parameterGroups, 1);
    assertCount(parameters, 0);
  }

  @Test
  public void stopOnParameter() {
    AtomicInteger configs = new AtomicInteger(0);
    AtomicInteger operations = new AtomicInteger(0);
    AtomicInteger sources = new AtomicInteger(0);
    AtomicInteger parameterGroups = new AtomicInteger(0);
    AtomicInteger parameters = new AtomicInteger(0);
    AtomicInteger providers = new AtomicInteger(0);

    new DeclarationWalker() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        configs.incrementAndGet();
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        operations.incrementAndGet();
      }

      @Override
      public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
        providers.incrementAndGet();
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        sources.incrementAndGet();
      }

      @Override
      public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
        parameterGroups.incrementAndGet();
      }

      @Override
      public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                              ParameterDeclaration declaration) {
        assertThat(parameterGroup, is(sameInstance(DeclarationWalkerTestCase.this.parameterGroup)));
        parameters.incrementAndGet();
        stop();
      }
    }.walk(extension);

    assertCount(configs, 1);
    assertCount(operations, 0);
    assertCount(sources, 0);
    assertCount(providers, 1);
    assertCount(parameterGroups, 1);
    assertCount(parameters, 1);
  }

  private void assertCount(AtomicInteger actual, int expected) {
    assertThat(actual.get(), is(expected));
  }
}

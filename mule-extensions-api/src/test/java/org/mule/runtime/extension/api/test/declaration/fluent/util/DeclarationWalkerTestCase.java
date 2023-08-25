/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.declaration.fluent.util;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConstructDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceCallbackDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithConstructsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithSourcesDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeclarationWalkerTestCase {

  @Mock
  private ExtensionDeclaration extension;

  @Mock
  private ConfigurationDeclaration configuration;

  @Mock
  private OperationDeclaration operation;

  @Mock
  private ConstructDeclaration construct;

  @Mock
  private ConnectionProviderDeclaration connectionProvider;

  @Mock
  private ParameterGroupDeclaration parameterGroup;

  @Mock
  private ParameterDeclaration parameter;

  @Mock
  private SourceDeclaration source;

  @Mock
  private SourceCallbackDeclaration sourceCallback;

  @Before
  public void before() {
    when(source.getSuccessCallback()).thenReturn(Optional.of(sourceCallback));
    when(source.getErrorCallback()).thenReturn(Optional.empty());

    when(extension.getConfigurations()).thenReturn(asList(configuration));
    when(extension.getConstructs()).thenReturn(asList(construct));
    when(extension.getOperations()).thenReturn(asList(operation));
    when(extension.getMessageSources()).thenReturn(asList(source));
    when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(configuration.getOperations()).thenReturn(asList(operation));
    when(configuration.getMessageSources()).thenReturn(asList(source));
    when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));
    when(parameterGroup.getParameters()).thenReturn(asList(parameter));

    addParameter(configuration, operation, construct, connectionProvider, source, sourceCallback);
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
    AtomicInteger constructs = new AtomicInteger(0);
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
      protected void onConstruct(WithConstructsDeclaration owner, ConstructDeclaration declaration) {
        constructs.incrementAndGet();
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
    assertCount(constructs, 1);
    assertCount(sources, 2);
    assertCount(providers, 2);
    assertCount(parameterGroups, 10);
    assertCount(parameters, 10);
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
    assertCount(parameterGroups, 4);
    assertCount(parameters, 4);
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

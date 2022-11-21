/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.declaration.fluent.ConnectedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConstructDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.FunctionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithConstructsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithFunctionsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithSourcesDeclaration;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher.DeclarationEnricherWalkDelegate;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A {@link DeclarationEnricherWalkDelegate} which assures that each component is visited only once, making it easy to handle the
 * fact that some components such as {@link OperationDeclaration}, {@link SourceDeclaration},
 * {@link ConnectionProviderDeclaration}, etc, implement the flyweight pattern, which means that the same instance might be
 * present at different levels. This is the delegate equivalent of {@link IdempotentDeclarationWalker} and should be used whenever
 * a traditional walker would use it.
 * <p>
 * The use of this delegate makes it unnecessary to manually control if a given component has already been seen.
 *
 * @since 1.5
 */
public class IdempotentDeclarationEnricherWalkDelegate extends DeclarationEnricherWalkDelegate {

  private Set<Reference<SourceDeclaration>> sources = new LinkedHashSet<>();
  private Set<Reference<ParameterDeclaration>> parameters = new LinkedHashSet<>();
  private Set<Reference<ParameterGroupDeclaration>> parameterGroups = new LinkedHashSet<>();
  private Set<Reference<OperationDeclaration>> operations = new LinkedHashSet<>();
  private Set<Reference<FunctionDeclaration>> functions = new HashSet<>();
  private Set<Reference<ConstructDeclaration>> constructs = new LinkedHashSet<>();
  private Set<Reference<ConnectionProviderDeclaration>> connectionProviders = new LinkedHashSet<>();

  private <T> boolean isFirstAppearance(Set<Reference<T>> accumulator, T item) {
    return accumulator.add(new Reference<>(item));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
    doOnce(sources, declaration, this::onSource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
    doOnce(parameterGroups, declaration, this::onParameterGroup);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                          ParameterDeclaration declaration) {
    doOnce(parameters, declaration, p -> onParameter(parameterGroup, declaration));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
    doOnce(operations, declaration, this::onOperation);
  }

  @Override
  public void onFunction(WithFunctionsDeclaration owner, FunctionDeclaration model) {
    doOnce(functions, model, this::onFunction);
  }

  @Override
  public void onConstruct(WithConstructsDeclaration owner, ConstructDeclaration declaration) {
    doOnce(constructs, declaration, this::onConstruct);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
    doOnce(connectionProviders, declaration, this::onConnectionProvider);
  }

  private <T> void doOnce(Set<Reference<T>> accumulator, T item, Consumer<T> delegate) {
    if (isFirstAppearance(accumulator, item)) {
      delegate.accept(item);
    }
  }

  /**
   * Invoked when an {@link ConnectedDeclaration} is found in the traversed {@code extensionDeclaration}.
   * <p>
   * This method will only be invoked once per each found instance
   *
   * @param declaration the {@link ConnectionProviderDeclaration}
   */
  protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {}

  /**
   * Invoked when an {@link SourceDeclaration} is found in the traversed {@code extensionDeclaration}
   * <p>
   * This method will only be invoked once per each found instance
   *
   * @param declaration the {@link SourceDeclaration}
   */
  protected void onSource(SourceDeclaration declaration) {}

  /**
   * Invoked when an {@link ParameterGroupDeclaration} is found in the traversed {@code extensionDeclaration}
   * <p>
   * This method will only be invoked once per each found instance
   *
   * @param declaration the {@link SourceDeclaration}
   */
  protected void onParameterGroup(ParameterGroupDeclaration declaration) {}

  /**
   * Invoked when an {@link ParameterDeclaration} is found in the traversed {@code extensionDeclaration}.
   * <p>
   * This method will only be invoked once per each found instance
   *
   * @param parameterGroup the {@link ParameterGroupDeclaration} in which the {@code declaration} is contained
   * @param declaration    the {@link ParameterDeclaration}
   */
  protected void onParameter(ParameterGroupDeclaration parameterGroup, ParameterDeclaration declaration) {}

  /**
   * Invoked when an {@link OperationDeclaration} is found in the traversed {@code extensionDeclaration}.
   * <p>
   * This method will only be invoked once per each found instance.
   *
   * @param declaration the {@link WithOperationsDeclaration}
   */
  protected void onOperation(OperationDeclaration declaration) {}

  /**
   * Invoked when a {@link ConstructDeclaration} is found in the traversed {@code extensionDeclaration}.
   * <p>
   * This method will only be invoked once per each found instance.
   *
   * @param declaration the {@link WithOperationsDeclaration}
   */
  protected void onConstruct(ConstructDeclaration declaration) {}

  /**
   * Invoked when an {@link FunctionDeclaration} is found in the traversed {@code extensionModel}.
   * <p>
   * This method will only be invoked once per each found instance
   *
   * @param declaration the {@link FunctionDeclaration}
   */
  protected void onFunction(FunctionDeclaration declaration) {}
}

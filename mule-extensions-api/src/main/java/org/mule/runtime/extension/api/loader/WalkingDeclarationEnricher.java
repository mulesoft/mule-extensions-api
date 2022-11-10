/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
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
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;

import java.util.Optional;

public interface WalkingDeclarationEnricher extends DeclarationEnricher {

  @Override
  default void enrich(ExtensionLoadingContext extensionLoadingContext) {
    getWalker(extensionLoadingContext).ifPresent(delegate -> {
      new DeclarationWalker() {

        @Override
        protected void onConfiguration(ConfigurationDeclaration declaration) {
          delegate.onConfiguration(declaration);
        }

        @Override
        protected void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
          delegate.onOperation(owner, declaration);
        }

        @Override
        protected void onFunction(WithFunctionsDeclaration owner, FunctionDeclaration declaration) {
          delegate.onFunction(owner, declaration);
        }

        @Override
        protected void onConstruct(WithConstructsDeclaration owner, ConstructDeclaration declaration) {
          delegate.onConstruct(owner, declaration);
        }

        @Override
        protected void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {
          delegate.onConnectionProvider(owner, declaration);
        }

        @Override
        protected void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
          delegate.onSource(owner, declaration);
        }

        @Override
        protected void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {
          delegate.onParameterGroup(owner, declaration);
        }

        @Override
        protected void onParameter(ParameterizedDeclaration owner, ParameterGroupDeclaration parameterGroup,
                                   ParameterDeclaration declaration) {
          delegate.onParameter(owner, parameterGroup, declaration);
        }
      }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
      delegate.onWalkFinished();
    });
  }

  /**
   * Enriches the descriptor provided in the given {@code extensionLoadingContext}.
   *
   * @param extensionLoadingContext a {@link ExtensionLoadingContext}
   */
  Optional<DeclarationEnricherWalkDelegate> getWalker(ExtensionLoadingContext extensionLoadingContext);


  class DeclarationEnricherWalkDelegate {

    /**
     * Invoked when a {@link ConfigurationDeclaration} is found in the traversed {@code extensionDeclaration}
     *
     * @param declaration a {@link ConfigurationDeclaration}
     */
    public void onConfiguration(ConfigurationDeclaration declaration) {}

    /**
     * Invoked when an {@link OperationDeclaration} is found in the traversed {@code extensionDeclaration}.
     * <p>
     *
     * @param owner       The declaration that owns the operation
     * @param declaration the {@link WithOperationsDeclaration}
     */
    public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {}

    /**
     * Invoked when an {@link FunctionDeclaration} is found in the traversed {@code extensionDeclaration}.
     * <p>
     *
     * @param owner The declaration that owns the function
     * @param model the {@link FunctionDeclaration}
     */
    public void onFunction(WithFunctionsDeclaration owner, FunctionDeclaration model) {}

    /**
     * Invoked when a {@link ConstructDeclaration} is found in the traversed {@code extensionDeclaration}.
     * <p>
     *
     * @param owner       The declaration that owns the operation
     * @param declaration the {@link WithOperationsDeclaration}
     */
    public void onConstruct(WithConstructsDeclaration owner, ConstructDeclaration declaration) {}

    /**
     * Invoked when an {@link ConnectedDeclaration} is found in the traversed {@code extensionDeclaration}
     *
     * @param owner       The declaration that owns the provider
     * @param declaration the {@link ConnectionProviderDeclaration}
     */
    public void onConnectionProvider(ConnectedDeclaration owner, ConnectionProviderDeclaration declaration) {}

    /**
     * Invoked when an {@link SourceDeclaration} is found in the traversed {@code extensionDeclaration}
     *
     * @param owner       The declaration that owns the source
     * @param declaration the {@link SourceDeclaration}
     */
    public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {}

    /**
     * Invoked when an {@link ParameterGroupDeclaration} is found in the traversed {@code extensionDeclaration}
     *
     * @param owner       The declaration that owns the parameter
     * @param declaration the {@link ParameterGroupDeclaration}
     */
    public void onParameterGroup(ParameterizedDeclaration owner, ParameterGroupDeclaration declaration) {}

    /**
     * Invoked when an {@link ParameterDeclaration} is found in the traversed {@code extensionDeclaration}
     *
     * @param owner          The declaration that owns the parameter
     * @param parameterGroup the group to which the declaration belongs
     * @param declaration    the {@link ParameterDeclaration}
     */
    public void onParameter(ParameterizedDeclaration owner,
                            ParameterGroupDeclaration parameterGroup,
                            ParameterDeclaration declaration) {}

    public void onWalkFinished() {}
  }
}

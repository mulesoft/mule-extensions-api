/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentDeclarationTypeName;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConstructDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;

import java.util.Optional;
import java.util.Set;

/**
 * Navigates all the components of the extension and automatically declares all complex types.
 *
 * @since 1.0
 */
public final class ExtensionTypesDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalker(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclarer declarer = extensionLoadingContext.getExtensionDeclarer();
    // subtypes have to be declared first so that any enrichment already done on them is available on the types from the
    // extension. Otherwise, the unenriched types from the parameters end up in the types.
    declareSubTypes(declarer);
    declarer.getDeclaration().getNotificationModels().forEach(notification -> registerType(declarer, notification.getType()));

    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      @Override
      public void onSource(SourceDeclaration declaration) {
        registerParametersType(declaration);
        registerType(declarer, declaration);
      }

      @Override
      public void onOperation(OperationDeclaration declaration) {
        registerParametersType(declaration);
        registerType(declarer, declaration);
      }

      @Override
      protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {
        registerParametersType(declaration);
      }

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        registerParametersType(declaration);
      }

      @Override
      protected void onConstruct(ConstructDeclaration declaration) {
        registerParametersType(declaration);
      }

      private void registerParametersType(ParameterizedDeclaration<? extends ParameterizedDeclaration> parameterizedDeclaration) {
        for (ParameterDeclaration parameterDeclaration : parameterizedDeclaration.getAllParameters()) {
          registerType(declarer, parameterDeclaration.getType());
        }
      }
    });
  }

  private void declareSubTypes(ExtensionDeclarer declarer) {
    declarer.getDeclaration().getSubTypes().forEach(type -> {
      registerType(declarer, type.getBaseType());
      registerTypes(declarer, type.getSubTypes());
    });
  }

  private void registerTypes(ExtensionDeclarer declarer, Set<ObjectType> types) {
    types.forEach(type -> registerType(declarer, type));
  }

  private void registerType(ExtensionDeclarer declarer, ExecutableComponentDeclaration<?> declaration) {
    if (declaration.getOutput() == null) {
      throw new IllegalModelDefinitionException(format("%s '%s' doesn't specify an output type",
          getComponentDeclarationTypeName(declaration), declaration.getName()));
    }

    if (declaration.getOutputAttributes() == null) {
      throw new IllegalModelDefinitionException(format("%s '%s' doesn't specify output attributes types",
          getComponentDeclarationTypeName(declaration), declaration.getName()));
    }

    registerType(declarer, declaration.getOutput().getType());
    registerType(declarer, declaration.getOutputAttributes().getType());
  }

  private void registerType(ExtensionDeclarer declarer, MetadataType type) {
    ExtensionMetadataTypeUtils.registerType(declarer, type);
  }
}
